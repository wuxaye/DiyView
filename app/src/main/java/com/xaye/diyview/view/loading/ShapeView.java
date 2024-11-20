package com.xaye.diyview.view.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ShapeView extends View {
    private ShapeType currentShapeType = ShapeType.CIRCLE;
    private Paint mPaint;
    private Path mPath;//画三角形
    private int triangleColor = 0xAA72D572;
    private int circleColor = 0xAA738FFE;
    private int squareColor = 0xAAE84E40;


    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //确保是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(width, height);

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (currentShapeType){
            case CIRCLE:
                int center = getWidth() / 2;
                mPaint.setColor(circleColor);
                canvas.drawCircle(center,center,center,mPaint);
                break;
            case SQUARE:
                mPaint.setColor(squareColor);
                canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
                break;
            case TRIANGLE://三角形
                mPaint.setColor(triangleColor);
                if (mPath == null) {
                    mPath = new Path();
                    mPath.moveTo(getWidth() / 2,0);
                    mPath.lineTo((float) 0, (float) ((getHeight() / 2) * Math.sqrt(3)));
                    mPath.lineTo(getWidth(),(float) ((getHeight() / 2) * Math.sqrt(3)));
                    mPath.close();
                }
                canvas.drawPath(mPath,mPaint);
                break;
        }
    }

    public void exchangeShape(){
        switch (currentShapeType){
            case CIRCLE:
                currentShapeType = ShapeType.SQUARE;
                break;
            case SQUARE:
                currentShapeType = ShapeType.TRIANGLE;
                break;
            case TRIANGLE:
                currentShapeType = ShapeType.CIRCLE;
                break;
        }
        invalidate();
    }

    public ShapeType getCurrentShapeType() {
        return currentShapeType;
    }

    public enum ShapeType{
        CIRCLE, //圆形
        SQUARE, //正方形
        TRIANGLE //三角形
    }
}
