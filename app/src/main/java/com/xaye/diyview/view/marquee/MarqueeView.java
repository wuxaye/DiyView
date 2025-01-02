package com.xaye.diyview.view.marquee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Author xaye
 *
 * @date: 2024/12/31
 */
public class MarqueeView extends View {
    private Paint textPaint;
    private Paint backgroundPaint;
    private RectF backgroundRect;

    private List<String> textList = new ArrayList<>();
    private int currentTextIndex = 0;
    private float textWidth;
    private float textHeight;
    private float currentX;

    public static final float SPEED_SLOW = 1f;
    public static final float SPEED_NORMAL = 3f;
    public static final float SPEED_FAST = 7f;
    private float speed = SPEED_NORMAL;

    public static final int DIRECTION_LEFT_TO_RIGHT = 1;
    public static final int DIRECTION_RIGHT_TO_LEFT = 2;
    private int direction = DIRECTION_RIGHT_TO_LEFT;

    public static final int ICON_POSITION_LEFT = 1;
    public static final int ICON_POSITION_RIGHT = 2;
    private int iconPosition = ICON_POSITION_LEFT;

    private Drawable iconDrawable;
    private int iconSize;

    private int backgroundColor = Color.LTGRAY;
    private float cornerRadius = 20f;
    private boolean isScrolling = true;

    public MarqueeView(Context context) {
        super(context);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);

        backgroundRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        backgroundRect.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint);

        if (!textList.isEmpty()) {
            String currentText = textList.get(currentTextIndex);
            textWidth = textPaint.measureText(currentText);

            float textStartX = currentX + cornerRadius;
            float textStartY = textHeight;

            // 绘制图标
            if (iconDrawable != null) {
                int iconLeft, iconTop;
                if (iconPosition == ICON_POSITION_LEFT) {
                    iconLeft = (int) cornerRadius;
                    textStartX += iconSize + 20;
                } else {
                    iconLeft = getWidth() - iconSize - (int) cornerRadius;
                }
                iconTop = (getHeight() - iconSize) / 2;
                iconDrawable.setBounds(iconLeft, iconTop, iconLeft + iconSize, iconTop + iconSize);
                iconDrawable.draw(canvas);
            }

            // 限制文字绘制区域
            float maxTextWidth = getWidth() - 2 * cornerRadius - (iconDrawable == null ? 0 : iconSize + 20);
            canvas.save();
            canvas.clipRect(cornerRadius, 0, getWidth() - cornerRadius, getHeight());

            // 绘制文本
            canvas.drawText(currentText, textStartX, textStartY, textPaint);

            // 滚动逻辑
            if (isScrolling) {
                if (direction == DIRECTION_LEFT_TO_RIGHT) {
                    currentX += speed;
                    if (currentX > getWidth() - cornerRadius) {
                        switchToNextText();
                    }
                } else {
                    currentX -= speed;
                    if (currentX + textWidth < cornerRadius) {
                        switchToNextText();
                    }
                }
                postInvalidateOnAnimation();
            }

            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!textList.isEmpty()) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            textHeight = (getMeasuredHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
        }

        // 自动调整图标大小
        if (iconDrawable != null) {
            iconSize = Math.min(getHeight() - (int) (2 * cornerRadius), getWidth() / 5);
        }
    }

    private void switchToNextText() {
        currentTextIndex = (currentTextIndex + 1) % textList.size();
        currentX = direction == DIRECTION_LEFT_TO_RIGHT ? -textWidth : getWidth();
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
        currentTextIndex = 0;
        currentX = direction == DIRECTION_LEFT_TO_RIGHT ? -textWidth : getWidth();
        invalidate();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
    }

    public void setDirection(int newDirection) {
        this.direction = newDirection;
    }

    public void setIcon(@DrawableRes int iconResId, int position) {
        this.iconDrawable = ContextCompat.getDrawable(getContext(), iconResId);
        this.iconPosition = position;
        invalidate();
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        invalidate();
    }

    public void startScroll() {
        isScrolling = true;
        postInvalidateOnAnimation();
    }

    public void stopScroll() {
        isScrolling = false;
    }
}
