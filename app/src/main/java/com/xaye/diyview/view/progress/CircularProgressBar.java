package com.xaye.diyview.view.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xaye.diyview.R;

/**
 * Author xaye
 *
 * @date: 2024/11/13
 */
public class CircularProgressBar extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF rectF;

    private int maxProgress = 100;
    private int progress = 0;
    private int circleBackgroundColor = Color.GRAY;
    private int progressColor = Color.GREEN;
    private int circleWidth = 20;
    private boolean showProgressText = true;
    private int progressTextColor = Color.BLACK;
    private int progressTextSize = 50;
    private int startAngle = 0; // 起始角度，默认从0开始

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化自定义属性
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
                progressColor = typedArray.getColor(R.styleable.CircularProgressBarEx_progressColor, Color.GREEN);
                circleWidth = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBarEx_circleWidth, 20);
                showProgressText = typedArray.getBoolean(R.styleable.CircularProgressBarEx_showProgressText, true);
                progressTextColor = typedArray.getColor(R.styleable.CircularProgressBarEx_progressTextColor, Color.BLACK);
                progressTextSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBarEx_progressTextSize, 50);

                // 获取自定义的起始角度
                int angleValue = typedArray.getInt(R.styleable.CircularProgressBarEx_startAngle, 0);
                switch (angleValue) {
                    case 90:
                        startAngle = 90;
                        break;
                    case 180:
                        startAngle = 180;
                        break;
                    case 270:
                        startAngle = 270;
                        break;
                    default:
                        startAngle = 0;
                        break;
                }
            } finally {
                typedArray.recycle();
            }
        }

        // 设置画笔属性
        backgroundPaint = new Paint();
        backgroundPaint.setColor(circleBackgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(circleWidth);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(circleWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//圆角
        progressPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(progressTextColor);
        textPaint.setTextSize(progressTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);//文本水平居中，并不能垂直居中
        textPaint.setAntiAlias(true);

        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = circleWidth / 2;
        rectF.set(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景圆环
        canvas.drawArc(rectF, 0f, 360f, false, backgroundPaint);

        // 绘制进度圆环
        float sweepAngle = 360f * progress / (float) maxProgress;

        // 根据起始角度调整起始位置
        float adjustedStartAngle = 0f;
        switch (startAngle) {
            case 90:
                adjustedStartAngle = 0f;
                break;
            case 180:
                adjustedStartAngle = 90f;
                break;
            case 270:
                adjustedStartAngle = 180f;
                break;
            default:
                adjustedStartAngle = -90f; // 默认从0度（顶部）开始
                break;
        }

        canvas.drawArc(rectF, adjustedStartAngle, sweepAngle, false, progressPaint);

        // 绘制进度文本
        if (showProgressText) {
            String progressText = progress + "%";

            // 使用视图的中心来计算x和y坐标
            float x = getWidth() / 2f;
            float y = getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;

            // 绘制文本
            canvas.drawText(progressText, x, y, textPaint);
        }
    }

    // 设置进度
    public void setProgress(int progress) {
        if (progress > maxProgress) {
            this.progress = maxProgress;
        } else {
            this.progress = Math.max(progress, 0);
        }
        invalidate(); // 重新绘制
    }


    // 获取当前进度
    public int getProgress() {
        return progress;
    }
}
