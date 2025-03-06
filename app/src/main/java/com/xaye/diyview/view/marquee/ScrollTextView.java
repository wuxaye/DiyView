package com.xaye.diyview.view.marquee;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

import com.xaye.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author xaye
 *
 * @date: 2025/3/6
 */
public class ScrollTextView extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "ScrollTextView";
    private SurfaceHolder surfaceHolder;// SurfaceHolder管理SurfaceView的绘制表面
    private Paint paint = null;
    private boolean stopScroll = false;// 停止滚动
    private boolean pauseScroll = false;// 暂停滚动
    private boolean clickEnable = false;// 是否启用点击事件
    public boolean isHorizontal = true;// 是否水平滚动
    private int speed = 4;// 滚动速度
    private long stayTimes = 5_000;// 横向滚动每段文字结束后的停留时间
    private List<String> textList = new ArrayList<>(); // 存储多个文本内容
    private int currentTextIndex = 0; // 当前滚动的文本索引
    private float textSize = 20f;// 文本字体大小
    private int textColor;// 文本颜色
    private int textBackColor = 0x00000000;// 文本背景颜色
    private int needScrollTimes = Integer.MAX_VALUE;// 需要滚动的次数
    private int viewWidth = 0, viewHeight = 0;// 视图的宽度和高度
    private float textWidth = 0f, textX = 0f, textY = 0f;// 文本的宽度、X坐标、Y坐标
    private float viewWidthPlusTextLength = 0.0f;// 视图宽度加上文本长度
    boolean isSetNewText = false;// 是否设置了新的文本
    boolean isScrollForever = true;// 是否无限滚动
    private Canvas canvas;// 画布，用于绘制文本
    private OnTextClickListener onTextClickListener;// 点击事件监听器

    public interface OnTextClickListener {
        void onTextClick(int index, String text);
    }

    public void setOnTextClickListener(OnTextClickListener listener) {
        this.onTextClickListener = listener;
    }

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = this.getHolder();  //get The surface holder
        surfaceHolder.addCallback(this);
        paint = new Paint();
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollTextView);
        clickEnable = arr.getBoolean(R.styleable.ScrollTextView_clickEnable, clickEnable);
        isHorizontal = arr.getBoolean(R.styleable.ScrollTextView_isHorizontal, isHorizontal);
        speed = arr.getInteger(R.styleable.ScrollTextView_speed, speed);
        textColor = arr.getColor(R.styleable.ScrollTextView_stTextColor, Color.BLACK);
        textSize = arr.getDimension(R.styleable.ScrollTextView_stTextSize, textSize);
        needScrollTimes = arr.getInteger(R.styleable.ScrollTextView_times, Integer.MAX_VALUE);
        isScrollForever = arr.getBoolean(R.styleable.ScrollTextView_isScrollForever, true);

        paint.setColor(textColor);
        paint.setTextSize(textSize);

        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        setZOrderOnTop(true);  //控制表面视图的表面位于其窗口的顶部。
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mHeight = getFontHeight(textSize);      //实际的视图高
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 当布局的宽度或高度是wrap_content，应该初始化ScrollTextView的宽度/高度
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, mHeight);
            viewHeight = mHeight;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, viewHeight);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, mHeight);
            viewHeight = mHeight;
        }
    }


    /**
     * surfaceChanged
     */
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d(TAG, "arg0:" + arg0.toString() + "  arg1:" + arg1 + "  arg2:" + arg2 + "  arg3:" + arg3);
    }

    /**
     * 界面出现，初始化一个新的滚动线程。
     *
     * @param holder holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        stopScroll = false;
        new Thread(new ScrollTextThread()).start();
        Log.d(TAG, "ScrollTextTextView is created");
    }

    /**
     * 界面退出，视图消失，自动调用
     *
     * @param arg0 SurfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        synchronized (this) {
            stopScroll = true;
        }
        Log.d(TAG, "ScrollTextTextView is destroyed");
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public int getBackgroundColor() {
        return textBackColor;
    }

    public void setScrollTextBackgroundColor(int color) {
        this.setBackgroundColor(color);
        this.textBackColor = color;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCurrentTextIndex() {
        return currentTextIndex;
    }

    public void setTimes(int times) {
        if (times <= 0) {
            throw new IllegalArgumentException("times was invalid integer, it must between > 0");
        } else {
            needScrollTimes = times;
            isScrollForever = false;
        }
    }

    public void setTextSize(float textSizeTem) {
        if (textSizeTem < 20) {
            throw new IllegalArgumentException("textSize must  > 20");
        } else if (textSizeTem > 900) {
            throw new IllegalArgumentException("textSize must  < 900");
        } else {
            this.textSize = sp2px(getContext(), textSizeTem);
            //重新设置Size
            paint.setTextSize(textSize);
            //视图区域也要改变
            measureTextParams(true);
            //实际的视图高
            int mHeight = getFontHeight(textSizeTem);
            android.view.ViewGroup.LayoutParams lp = this.getLayoutParams();
            lp.width = viewWidth;
            lp.height = dip2px(this.getContext(), mHeight);
            this.setLayoutParams(lp);

            isSetNewText = true;
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    // 设置多个文本内容
    public void setTextList(List<String> textList) {
        this.textList = textList;
        this.currentTextIndex = 0;
        isSetNewText = true;
        stopScroll = false;
        measureTextParams(true);
    }

    public void setTextColor(@ColorInt int color) {
        textColor = color;
        paint.setColor(textColor);
    }


    /**
     * 设置滚动速度
     *
     * @param speed SCROLL SPEED [4,14] ///// 0?
     */
    public void setSpeed(int speed) {
        if (speed > 14 || speed < 4) {
            throw new IllegalArgumentException("Speed was invalid integer, it must between 4 and 14");
        } else {
            this.speed = speed;
        }
    }

    /**
     * 设置是否永远滚动文本
     */
    public void setScrollForever(boolean scrollForever) {
        isScrollForever = scrollForever;
    }

    public boolean isPauseScroll() {
        return pauseScroll;
    }

    public void setPauseScroll(boolean pauseScroll) {
        this.pauseScroll = pauseScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!clickEnable) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll = !pauseScroll;
                if (onTextClickListener != null) {
                    onTextClickListener.onTextClick(currentTextIndex, textList.get(currentTextIndex));
                }
                break;
        }
        return true;
    }

    private void drawVerticalScroll() {
        if (currentTextIndex >= textList.size()) return;
        // 计算基线
        String text = textList.get(currentTextIndex);
        float fontHeight = paint.getFontMetrics().bottom - paint.getFontMetrics().top;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseLine = (float) viewHeight / 2 + distance;
        // 是否需要水平滚动
        boolean needHorizontalScroll = paint.measureText(text) > viewWidth;
        // 水平滚动偏移
        float horizontalOffset = 0;

        // --- 阶段1：垂直滚动到基线位置（精准停止）---
        float currentVerticalPos = viewHeight + fontHeight; // 初始位置
        while (currentVerticalPos > baseLine) {
            if (stopScroll || isSetNewText) return;
            if (pauseScroll) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            // 计算下一步位置，避免越过基线
            float nextPos = currentVerticalPos - 3;
            if (nextPos < baseLine) nextPos = baseLine; // 确保不会越过基线
            drawText(0, nextPos);
            currentVerticalPos = nextPos;
        }

        // --- 阶段1暂停：基线位置暂停 ---
        if (needHorizontalScroll) {
            sleep(1000);
        } else if (!stopScroll && !isSetNewText) {
            sleep(stayTimes); // 原基线位置暂停
        }

        // --- 阶段2：水平滚动（垂直位置固定为基线）---
        if (needHorizontalScroll) {
            // 计算最大水平滚动偏移
            float maxHorizontalOffset = -(paint.measureText(text) - viewWidth);
            // 水平滚动
            while (horizontalOffset > maxHorizontalOffset) {

                if (stopScroll || isSetNewText) return;
                horizontalOffset -= speed;
                drawText(horizontalOffset, baseLine);
            }

            // --- 阶段2暂停：水平滚动完成后暂停 ---
            if (!stopScroll && !isSetNewText) {
                sleep(stayTimes); // 新增水平滚动完成后的暂停
            }
        }

        // --- 阶段3：继续垂直滚动到视图外 ---
        for (float i = baseLine; i > -fontHeight; i -= 3) {
            if (stopScroll || isSetNewText) return;
            if (pauseScroll) {
                sleep(1000);
                continue;
            }
            drawText(needHorizontalScroll ? horizontalOffset : 0, i);
        }

        // 切换到下一个文本
        currentTextIndex++;
        if (currentTextIndex >= textList.size()) {
            if (isScrollForever) currentTextIndex = 0;
            else stopScroll = true;
        }
    }

    /**
     * 绘制文本
     */
    private synchronized void drawText(float x, float y) {
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas == null) return;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawText(textList.get(currentTextIndex), x, y, paint);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.setVisibility(visibility);
    }

    // 测量文本参数
    private void measureTextParams(boolean isInitialSetup) {
        if (currentTextIndex >= textList.size()) return;
        textWidth = paint.measureText(textList.get(currentTextIndex));
        viewWidthPlusTextLength = viewWidth + textWidth;
        if (isInitialSetup) {
            textX = viewWidth - viewWidth / 2; // 第一次创建 ，默认从居中的位置开始滚动
            Paint.FontMetrics fm = paint.getFontMetrics();
            float distance = (fm.bottom - fm.top) / 2 - fm.bottom;
            textY = viewHeight / 2 + distance;
        }
    }


    /**
     * Scroll thread
     */
    class ScrollTextThread implements Runnable {
        @Override
        public void run() {
            measureTextParams(true);

            while (!stopScroll && surfaceHolder != null && !Thread.currentThread().isInterrupted()) {

                if (isHorizontal) { // 水平滚动逻辑

                    if (pauseScroll) {// 暂停
                        sleep(1000);
                        continue;
                    }

                    drawText(viewWidth - textX, textY);// 绘制文本
                    textX += speed;// 文本滚动距离
                    if (textX > viewWidthPlusTextLength) {// 文本从右至左滚动超出屏幕
                        currentTextIndex++;
                        textX = 0;
                        --needScrollTimes;

                        if (currentTextIndex >= textList.size()) {// 文字循环展示
                            if (isScrollForever) {
                                currentTextIndex = 0;
                            } else {
                                stopScroll = true;
                                break;
                            }
                        }
                        measureTextParams(false);
                    }
                } else { // 垂直滚动逻辑
                    drawVerticalScroll();
                    isSetNewText = false;
                    --needScrollTimes;
                }

                if (needScrollTimes <= 0 && isScrollForever) { // 滚动次数小于0，则滚动结束
                    stopScroll = true;
                }
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}
