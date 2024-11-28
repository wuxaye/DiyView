package com.xaye.diyview.view.alphabet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;

/**
 * Author xaye
 *
 * @date: 2024/11/28
 */
public class AlphabetTouchView extends View {
    private static final String DEFAULT_ALPHABET  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    private String mAlphabet;
    private Paint mTextPaint;
    private Paint mBubblePaint;
    private int mTextSize;
    private int mTextColor;
    private int mSelectedTextColor = Color.RED;
    private int mWidth, mHeight;
    private float mLetterHeight;
    private int mSelectedIndex = -1;

    private OnLetterSelectedListener mListener;

    public void setOnLetterSelectedListener(OnLetterSelectedListener listener) {
        mListener = listener;
    }


    public AlphabetTouchView(Context context) {
        this(context, null);
    }

    public AlphabetTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphabetTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AlphabetTouchView,
                0, 0);

        try {
            mTextSize = a.getDimensionPixelSize(R.styleable.AlphabetTouchView_textSize, 14);
            mTextColor = a.getColor(R.styleable.AlphabetTouchView_textColor, Color.BLACK);
            mSelectedTextColor = a.getColor(R.styleable.AlphabetTouchView_selectedTextColor, Color.RED);
            mAlphabet = a.getString(R.styleable.AlphabetTouchView_alphabet);
            if (mAlphabet == null || mAlphabet.isEmpty()) {
                mAlphabet = DEFAULT_ALPHABET;
            }
        } finally {
            a.recycle();
        }


        init();
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(sp2px(mTextSize));
        mTextPaint.setAntiAlias(true);

        mBubblePaint = new Paint();
        mBubblePaint.setColor(Color.parseColor("#AA0000"));
        mBubblePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mLetterHeight = (float) mHeight / mAlphabet.length();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制26个字母

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics(); // 获取字体的度量信息
        float fontHeight = fontMetrics.descent - fontMetrics.ascent; // 字体高度

        for (int i = 0; i < mAlphabet.length(); i++) {
            // 计算字母的x坐标，使字母居中 ,x = mWidth / 2 - 文字的宽度 / 2
            float x = mWidth / 2 - mTextPaint.measureText(String.valueOf(mAlphabet.charAt(i))) / 2;

            // 计算字母的y坐标，使用均匀分布的间距
            // i * mLetterHeight: 当前字母所在的格子的顶部位置
            // (mLetterHeight + fontHeight) / 2 : 计算字母在格子中应该偏移多少才能垂直居中
            // - fontMetrics.descent : 确保基线（baseline）正确对齐,fontMetrics.descent是从基线到字母底部的距离，所以减去这一部分能使得字母真正的中心对齐到格子的中心。
            float y = i * mLetterHeight + (mLetterHeight + fontHeight) / 2 - fontMetrics.descent;

            if (i == mSelectedIndex) {
                // 选中的字母文字颜色设置为选中颜色
                mTextPaint.setColor(mSelectedTextColor);
            } else {
                // 否则，使用普通的文字颜色
                mTextPaint.setColor(mTextColor);
            }

            // 绘制字母
            canvas.drawText(String.valueOf(mAlphabet.charAt(i)), x, y, mTextPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float touchY = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 根据触摸的Y坐标来计算字母的索引
                // 获取触摸点所在的字母
                int index = (int) (touchY / (mHeight / mAlphabet.length()));
                if (index >= 0 && index < mAlphabet.length()) {
                    mSelectedIndex = index;
                    if (mListener != null) {
                        mListener.onLetterSelected(String.valueOf(mAlphabet.charAt(mSelectedIndex)),true);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.onLetterSelected(String.valueOf(mAlphabet.charAt(mSelectedIndex)),false);
                }
                mSelectedIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    public interface OnLetterSelectedListener {
        void onLetterSelected(String letter,boolean  isTouch);
    }


/*|---------------------------|  <- 格子顶部 (i * mLetterHeight)
|                           |
|        字母区域            |  <- 字母的绘制
|                           |
|---------------------------|  <- 格子底部 ((i + 1) * mLetterHeight)*/


}
