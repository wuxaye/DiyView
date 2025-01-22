package com.xaye.diyview.view.erase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

import com.xaye.diyview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Author xaye
 *
 * @date: 2025/1/22
 */
public class EraseView extends View {
    private Paint mPaint; // 擦除画笔
    private TextPaint mOuterTextPaint; // 最外层文字画笔
    private TextPaint mInnerTextPaint; // 内部文字画笔
    private Path currentPath; // 当前擦除路径
    private Bitmap srcBitmap; // 源图层，被擦的图层
    private Bitmap dstBitmap; // 目标图层, 执行擦的图层，dstBitmap 是一个透明背景的 Bitmap，它的内容由用户手指轨迹绘制（即 Path）决定。
    private Canvas dstCanvas; // 用于绘制擦除路径的画布

    private String innerText = ""; // 内部文字
    private String outerText = ""; // 最外层文字
    private boolean isShowResultDirect = false; // 是否直接显示结果

    private static final float REVEAL_THRESHOLD = 0.8f; // 显示目标的擦除比例阈值
    private @ColorInt int outerLayerColor; // 最外层颜色
    private @ColorInt int innerLayerColor; // 内部图层颜色

    private String[] prizeTexts = {"谢谢惠顾", "特等奖", "一等奖", "二等奖", "三等奖"}; // 默认奖项数组

    private OnEraseCompleteListener onEraseCompleteListener; // 回调接口

    public EraseView(Context context) {
        this(context, null);
    }

    public EraseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EraseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EraseView);

        // 获取自定义属性
        outerText = a.getString(R.styleable.EraseView_outerText);
        if (outerText == null) {
            outerText = "刮开我！"; // 如果没有在 XML 中指定属性，使用默认文本
        }
        float outerTextSize = a.getDimension(R.styleable.EraseView_outerTextSize, sp2px(24));
        float innerTextSize = a.getDimension(R.styleable.EraseView_innerTextSize, sp2px(24));
        int outerTextColor = a.getColor(R.styleable.EraseView_outerTextColor, Color.BLACK);
        int innerTextColor = a.getColor(R.styleable.EraseView_innerTextColor, Color.RED);
        outerLayerColor = a.getColor(R.styleable.EraseView_outerLayerColor, Color.parseColor("#BBBBBB"));
        innerLayerColor = a.getColor(R.styleable.EraseView_innerLayerColor, Color.LTGRAY);

        a.recycle();

        setLayerType(LAYER_TYPE_SOFTWARE, null); // 关闭硬件加速

        // 初始化擦除画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(20));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//两条直线连接处（拐角处）的外观:斜接、圆滑、斜面

        // 初始化最外层文字画笔
        mOuterTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mOuterTextPaint.setColor(outerTextColor);
        mOuterTextPaint.setTextSize(outerTextSize);

        // 初始化内部文字画笔
        mInnerTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mInnerTextPaint.setColor(innerTextColor);
        mInnerTextPaint.setTextSize(innerTextSize);

        currentPath = new Path();

        innerText = getRandomStr();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        srcBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        dstBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        dstCanvas = new Canvas(dstBitmap);

        // 在源图层上绘制灰色背景和文字
        Canvas canvas = new Canvas(srcBitmap);
        canvas.drawColor(outerLayerColor);

        // 绘制外层文字
        float textWidth = mOuterTextPaint.measureText(outerText);
        Paint.FontMetrics fontMetrics = mOuterTextPaint.getFontMetrics();
        float baselineOffset = (fontMetrics.bottom + fontMetrics.top) / 2;
        canvas.drawText(outerText, (w - textWidth) / 2, h / 2 - baselineOffset, mOuterTextPaint);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (srcBitmap == null || dstBitmap == null) return;

        // 绘制内层背景颜色
        canvas.drawColor(innerLayerColor);

        // 绘制内层文字
        float textWidth = mInnerTextPaint.measureText(innerText);
        Paint.FontMetrics fontMetrics = mInnerTextPaint.getFontMetrics();
        float baselineOffset = (fontMetrics.bottom + fontMetrics.top) / 2;
        canvas.drawText(innerText, (getWidth() - textWidth) / 2, getHeight() / 2 - baselineOffset, mInnerTextPaint);

        // 使用图层保存绘制状态
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);

        // 这一步将用户已经刮开的区域(srcBitmap + setXfermode 绘制的区域)绘制到离屏图层，作为混合模式的目标层。
        canvas.drawBitmap(dstBitmap, 0, 0, null); //每一次触摸操作会更新 dstBitmap，将透明路径涂抹到这个图层上。



        // 设置混合模式实现擦除效果，擦的是srcBitmap，执行擦的动作是dstBitmap，由于dstBitmap 是透明层，所以把 srcBitmap 擦完，
        // 就全部显示了透明层，也就显示出了最底层的内容！

        //PorterDuff.Mode.SRC_OUT：保留目标区域中 源图像以外 的内容，源图像的像素会“擦除”目标图像的像素。
        //PorterDuff.Mode.DST_OUT：保留目标区域中 源图像完全覆盖的部分，源图像的像素被抠出。
        mPaint.setXfermode(new PorterDuffXfermode(
                isShowResultDirect ? PorterDuff.Mode.DST_OUT : PorterDuff.Mode.SRC_OUT));
        //盖上最外层背景
        canvas.drawBitmap(srcBitmap, 0, 0, mPaint);//混合模式会根据用户的擦除路径（透明区域）动态裁剪 srcBitmap，只显示已经擦除的部分。



        // 清除混合模式,保证后续绘制操作不受混合模式的影响,因为 onDraw 会不断调用，mPaint 会被循环使用。
        mPaint.setXfermode(null);

        //恢复到调用 saveLayer 之前的画布状态，并将离屏图层的内容合成到主画布。
        canvas.restoreToCount(layerId);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                currentPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                dstCanvas.drawPath(currentPath, mPaint);
                invalidate();

                // 检查擦除比例
                if (getEraseRatio() >= REVEAL_THRESHOLD && !isShowResultDirect) {
                    isShowResultDirect = true;
                    invalidate();
                    if (onEraseCompleteListener != null) {
                        onEraseCompleteListener.onEraseComplete(innerText);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                currentPath.reset();
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    private float getEraseRatio() {
        int totalPixels = dstBitmap.getWidth() * dstBitmap.getHeight();
        int erasedPixels = 0;

        int[] pixels = new int[totalPixels];
        dstBitmap.getPixels(pixels, 0, dstBitmap.getWidth(), 0, 0, dstBitmap.getWidth(), dstBitmap.getHeight());
        for (int pixel : pixels) {
            if (pixel != 0) erasedPixels++;
        }
        return erasedPixels / (float) totalPixels;
    }

    public void setOnEraseCompleteListener(OnEraseCompleteListener listener) {
        this.onEraseCompleteListener = listener;
    }

    private String getRandomStr() {
        return prizeTexts[new Random().nextInt(prizeTexts.length)];
    }

    public void setPrizeTexts(String[] texts) {
        if (texts != null && texts.length > 0) {
            prizeTexts = texts;
            innerText = getRandomStr();
            invalidate();
        }
    }

    public interface OnEraseCompleteListener {
        void onEraseComplete(String result);
    }

    private int dp2px(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int sp2px(float sp) {
        return (int) (sp * getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
