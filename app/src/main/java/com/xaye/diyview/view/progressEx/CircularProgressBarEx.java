package com.xaye.diyview.view.progressEx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xaye.diyview.R;

/**
 * Author xaye
 *
 * @date: 2024/12/31
 */
public class CircularProgressBarEx extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF rectF;
    private Bitmap pointerBitmap; // 指针图标
    private float bitmapWidth; // Bitmap的宽度
    private float bitmapHeight; // Bitmap的高度
    private int outerSize = 5; //让指针漏出圆环的长度
    private int maxProgress = 100;
    private int progress = 0;
    private int circleBackgroundColor = Color.GRAY;
    private int[] progressColors = {Color.GREEN, Color.BLUE}; // 渐变颜色
    private int circleWidth = 20;
    private boolean showProgressText = true;
    private int progressTextColor = Color.BLACK;
    private int progressTextSize = 50;
    private int startAngle = 0; // 默认从左边开始
    private float centerX, centerY;
    private float radius;

    public static final int CLOCKWISE = 1;
    public static final int COUNTERCLOCKWISE = -1;
    private int direction = COUNTERCLOCKWISE; // 默认顺时针

    private LinearGradient gradientShader;

    public CircularProgressBarEx(Context context) {
        this(context, null);
    }

    public CircularProgressBarEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBarEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CircularProgressBarEx,
                    0, 0
            );
            try {
                maxProgress = typedArray.getInt(R.styleable.CircularProgressBarEx_maxProgress, 100);
                progress = typedArray.getInt(R.styleable.CircularProgressBarEx_progress, 0);
                circleBackgroundColor = typedArray.getColor(R.styleable.CircularProgressBarEx_circleBackgroundColor, Color.GRAY);
                circleWidth = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBarEx_circleWidth, 20);
                showProgressText = typedArray.getBoolean(R.styleable.CircularProgressBarEx_showProgressText, true);
                progressTextColor = typedArray.getColor(R.styleable.CircularProgressBarEx_progressTextColor, Color.BLACK);
                progressTextSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBarEx_progressTextSize, 50);
                startAngle = typedArray.getInt(R.styleable.CircularProgressBarEx_startAngle, 0);
                direction = typedArray.getInt(R.styleable.CircularProgressBarEx_direction, CLOCKWISE);
                outerSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBarEx_outerSize, 5);
            } finally {
                typedArray.recycle();
            }
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(circleBackgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(circleWidth);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(circleWidth);
        //progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(progressTextColor);
        textPaint.setTextSize(progressTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = circleWidth / 2 + outerSize;
        rectF.set(padding, padding, w - padding, h - padding);

        centerX = rectF.centerX();
        centerY = rectF.centerY();

        radius = rectF.width() / 2;
        updateGradient();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景圆环
        //canvas.drawArc(rectF, 0f, 360f, false, backgroundPaint);

        // 绘制背景圆环
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        // 计算进度角度
        float sweepAngle = 360f * progress / maxProgress;
        sweepAngle *= direction;

        // 绘制进度
        canvas.drawArc(rectF, startAngle, sweepAngle, false, progressPaint);

        // 绘制进度文本
        if (showProgressText) {
            String progressText = progress + "%";
            float x = getWidth() / 2f;
            float y = getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
            canvas.drawText(progressText, x, y, textPaint);
        }

        // 绘制指针
        if (pointerBitmap != null) {
            drawPointer(canvas, startAngle + sweepAngle);
        }

    }

    /**
     * 绘制指针
     *
     * @param angle 指针的角度（画布坐标系下的角度）
     */
    private void drawPointer(Canvas canvas, float angle) {

        //Log.d("drawPointer", "angle: " + angle);
        // 计算调整后的半径，使Bitmap边缘紧贴圆环最外部
        float adjustedRadius = radius + backgroundPaint.getStrokeWidth() / 2 + outerSize;

        // 确保Bitmap的右边上的中心点在圆上
        float rightCenterX = centerX + adjustedRadius * (float) Math.cos(Math.toRadians(angle));
        float rightCenterY = centerY + adjustedRadius * (float) Math.sin(Math.toRadians(angle));

        // 计算Bitmap左上角的位置，使得右边上的中心点位于计算出的坐标
        float left = rightCenterX - bitmapWidth;
        float top = rightCenterY - bitmapHeight / 2;

        // 保存画布状态
        canvas.save();

        // 将画布旋转，使得Bitmap对齐到半径上
        canvas.rotate(angle, rightCenterX, rightCenterY);

        // 绘制指针Bitmap
        canvas.drawBitmap(pointerBitmap, left, top, null);

        // 恢复画布状态
        canvas.restore();
    }


    // 更新渐变
    private void updateGradient() {
        float centerX = rectF.centerX();
        float centerY = rectF.centerY();
        float radius = rectF.width() / 2;

        double startAngleRadians = Math.toRadians(startAngle);

        float startX = centerX + (float) (radius * Math.cos(startAngleRadians));
        float startY = centerY + (float) (radius * Math.sin(startAngleRadians));
        float endX = centerX - (float) (radius * Math.cos(startAngleRadians));
        float endY = centerY - (float) (radius * Math.sin(startAngleRadians));

        //Log.d("updateGradient", "startX: " + startX + ", startY: " + startY + ", endX: " + endX + ", endY: " + endY);

        //线性渐变，从一个点渐变到另一个点，因为渐变的距离是圆的直径 所以，TileMode 在这里实际无意义
        gradientShader = new LinearGradient(
                startX, startY, endX, endY,
                progressColors, null,
                Shader.TileMode.CLAMP
        );
        progressPaint.setShader(gradientShader);
    }

    // 设置进度
    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(progress, maxProgress));
        invalidate();
    }

    // 设置指针图标
    public void setPointerBitmap(Bitmap bitmap) {
        this.pointerBitmap = bitmap;
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        invalidate();
    }


    // 设置渐变颜色
    public void setProgressColors(int[] colors) {
        this.progressColors = colors;
        updateGradient();
        invalidate();
    }

    // 设置绘制方向
    public void setDirection(int direction) {
        this.direction = direction;
        invalidate();
    }

    // 设置开始角度
    public void setStartAngle(int angle) {
        this.startAngle = angle;
        updateGradient();
        invalidate();
    }

    // 获取当前进度
    public int getProgress() {
        return progress;
    }
}
