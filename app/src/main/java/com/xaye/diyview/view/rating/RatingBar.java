package com.xaye.diyview.view.rating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;

/**
 * Author xaye
 *
 * @date: 2024/11/23
 */
public class RatingBar extends View {

    private Bitmap mStarNormalBitmap,mStarFocusBitmap;

    private int mGradeNum = 5;
    public RatingBar(Context context) {
        this(context,null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int startNormalId = typedArray.getResourceId(R.styleable.RatingBar_starNormal,0);
        if (startNormalId == 0) {
            throw new RuntimeException("请设置属性：starNormal");
        }

        int startFocusId = typedArray.getResourceId(R.styleable.RatingBar_starFocus,0);
        if (startFocusId == 0) {
            throw new RuntimeException("请设置属性：starFocus");
        }

        mGradeNum = typedArray.getInt(R.styleable.RatingBar_gradeNumber,5);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
