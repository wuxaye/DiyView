package com.xaye.diyview.view.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;


public class ColorTrackTextView extends androidx.appcompat.widget.AppCompatTextView {
    // 原始的画笔
    private Paint mOriginPaint;

    // 改变的画笔
    private Paint mChangePaint;

    // 当前进度值，范围为 0.0 到 1.0
    private float mProgress = 0f;

    // 动画方向，默认为从左到右
    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    // 初始化画笔对象
    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        // 获取用户设置的颜色，若未设置则使用默认颜色
        int initialColor = typedArray.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
        int changedColor = typedArray.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());

        // 创建两个画笔，一个用于初始颜色，一个用于变化后的颜色
        mOriginPaint = createPaint(initialColor);
        mChangePaint = createPaint(changedColor);

        typedArray.recycle();
    }

    // 根据颜色创建画笔对象
    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true); // 开启抗锯齿
        paint.setDither(true); // 开启抖动效果
        paint.setTextSize(getTextSize()); // 设置文本大小
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取文本内容和画布的宽高
        String text = getText().toString();
        int width = getWidth();
        int height = getHeight();

        // 获取文本的边界和基线位置
        Rect textBounds = new Rect();
        mOriginPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textWidth = textBounds.width();
        int x = width / 2 - textWidth / 2; // 计算文本绘制的起始 X 坐标

        Paint.FontMetrics fontMetrics = mOriginPaint.getFontMetrics();
        int baseline = height / 2 + (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom); // 计算基线 Y 坐标

        // 根据当前进度和方向绘制文本
        int middle = (int) (mProgress * width); // 当前进度对应的分割位置

        if (mDirection == Direction.LEFT_TO_RIGHT) {
            // 从左到右：左边是改变颜色，右边是初始颜色
            drawText(canvas, mChangePaint, x, baseline, 0, middle); // 绘制不变色部分
            drawText(canvas, mOriginPaint, x, baseline, middle, width); // 绘制变色部分
        } else if (mDirection == Direction.RIGHT_TO_LEFT) {
            // 从右到左：右边是改变颜色，左边是初始颜色
            drawText(canvas, mChangePaint, x, baseline, width - middle, width); // 绘制不变色部分
            drawText(canvas, mOriginPaint, x, baseline, 0, width - middle); // 绘制变色部分
        }
    }

    // 绘制指定区域的文本
    private void drawText(Canvas canvas, Paint paint, int x, int y, int left, int right) {
        canvas.save(); // 保存画布状态
        canvas.clipRect(left, 0, right, getHeight()); // 裁剪画布区域
        canvas.drawText(getText().toString(), x, y, paint); // 绘制文本
        canvas.restore(); // 恢复画布状态
    }

    // 设置文字的变化方向
    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    // 设置当前进度并刷新界面
    public void setProgress(float progress) {
        mProgress = progress;
        invalidate(); // 重绘视图
    }

    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }

    public void setOriginColor(int originColor) {
        this.mOriginPaint.setColor(originColor);
    }
}
