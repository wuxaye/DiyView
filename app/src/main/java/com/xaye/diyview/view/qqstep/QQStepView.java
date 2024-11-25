package com.xaye.diyview.view.qqstep;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;


public class QQStepView extends View {

    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20; //px
    private int mStepTextSize;
    private int mStepTextColor;

    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    private int mStepMax = 100;
    private int mStepCurrent = 0;


    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 1.分析效果
        // 2.确定自定义属性 attr.xml
        // 3.在布局文件中使用
        // 4.在自定义View 中获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterColor = array.getColor(R.styleable.QQStepView_outerColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mBorderWidth = array.getDimensionPixelSize(R.styleable.QQStepView_borderWidth, mBorderWidth);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        array.recycle();

        mOuterPaint = new Paint();
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStyle(Paint.Style.STROKE); //实心
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerPaint = new Paint();
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
        mTextPaint.setAntiAlias(true);

        // onMeasure
        // 6.画外圆弧 画内圆弧 画文字
        // 7.其他
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 布局可能是宽高 wrap_content
        //读取模式 AT_MOST 40dp

        // 宽高不一致 取最小值，确保是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(width, height);

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1.画外圆弧
        int center = getWidth() / 2;
        int radius = center - mBorderWidth / 2;

        //RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        canvas.drawArc(oval, 135, 270, false, mOuterPaint);

        // 2.画内圆弧
        if (mStepCurrent <= 0) return;
        float sweepAngle = (float) mStepCurrent / mStepMax * 270;
        canvas.drawArc(oval, 135, sweepAngle, false, mInnerPaint);

        // 3.画文字
        String stepText = mStepCurrent + "";

        //控件的一半 减去 文字的一半
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), bounds);
        int dx = getWidth() / 2 - bounds.width() / 2; // x轴偏移量

        //基线
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = center + dy; // y轴偏移量

        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }

    // 动起来
    public synchronized void setStep(int step) {
        mStepCurrent = step;
        invalidate();
    }

    public synchronized void setStepMax(int stepMax) {
        mStepMax = stepMax;
        invalidate();
    }
}
