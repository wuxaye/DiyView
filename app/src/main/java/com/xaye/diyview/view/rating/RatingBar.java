package com.xaye.diyview.view.rating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;

/**
 * Author xaye
 *
 * @date: 2024/11/23
 */
public class RatingBar extends View {

    private Bitmap mStarNormalBitmap, mStarFocusBitmap;

    private int mGradeNum = 5;

    private int mCurrentGrade = 0;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int startNormalId = typedArray.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (startNormalId == 0) {
            throw new RuntimeException("请设置属性：starNormal");
        }

        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), startNormalId);

        int startFocusId = typedArray.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (startFocusId == 0) {
            throw new RuntimeException("请设置属性：starFocus");
        }
        mStarFocusBitmap = BitmapFactory.decodeResource(getResources(), startFocusId);

        mGradeNum = typedArray.getInt(R.styleable.RatingBar_gradeNumber, 5);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度 一张图片的高度，加顶部和底部的padding值
        int height = mStarNormalBitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        // 宽度
        int width = mStarNormalBitmap.getWidth() * mGradeNum;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mGradeNum; i++) {
            // @NonNull Bitmap bitmap, float left, float top, @Nullable Paint paint

            if (mCurrentGrade > i) {
                canvas.drawBitmap(mStarFocusBitmap, i * mStarNormalBitmap.getWidth(), 0, null);
            } else {
                canvas.drawBitmap(mStarNormalBitmap, i * mStarNormalBitmap.getWidth(), 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 移动 按下 抬起 处理逻辑都一样， 根据手指位置 计算分数 再去刷新
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                float x = event.getX(); // x坐标, 相对于当前控件的, getRawX() 是相对于屏幕的
                float grade = x / mStarNormalBitmap.getWidth() + 1; // 分数

                if (grade < 0) {
                    grade = 0;
                }

                if (grade > mGradeNum) {
                    grade = mGradeNum;
                }

                if (mCurrentGrade != (int) grade) {
                    mCurrentGrade = (int) grade;
                    invalidate();
                    Log.i("TAG", "分数：" + mCurrentGrade);
                }
                break;
            default:
                break;
        }


        return true;
    }
}
