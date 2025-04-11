package com.xaye.diyview.view.circleseekbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.xaye.diyview.R;

public class CircleSeekBar extends View {
    private static final double RADIAN = 180 / Math.PI;//弧度

    private static final String INATANCE_STATE = "state";//状态
    private static final String INSTANCE_MAX_PROCESS = "max_process";//最大进度
    private static final String INSTANCE_CUR_PROCESS = "cur_process";//当前进度
    private static final String INSTANCE_REACHED_COLOR = "reached_color";//已完成进度颜色
    private static final String INSTANCE_REACHED_WIDTH = "reached_width";//已完成进度宽度
    private static final String INSTANCE_REACHED_CORNER_ROUND = "reached_corner_round";//已完成进度圆角
    private static final String INSTANCE_UNREACHED_COLOR = "unreached_color";//未完成进度颜色
    private static final String INSTANCE_UNREACHED_WIDTH = "unreached_width";//未完成进度宽度
    private static final String INSTANCE_POINTER_COLOR = "pointer_color";//指针颜色
    private static final String INSTANCE_POINTER_RADIUS = "pointer_radius";//指针半径
    private static final String INSTANCE_POINTER_SHADOW = "pointer_shadow";//指针阴影
    private static final String INSTANCE_POINTER_SHADOW_RADIUS = "pointer_shadow_radius";//指针阴影半径
    private static final String INSTANCE_WHEEL_SHADOW = "wheel_shadow";//圆环阴影
    private static final String INSTANCE_WHEEL_SHADOW_RADIUS = "wheel_shadow_radius";//圆环阴影半径
    private static final String INSTANCE_WHEEL_HAS_CACHE = "wheel_has_cache";//圆环是否有缓存
    private static final String INSTANCE_WHEEL_CAN_TOUCH = "wheel_can_touch";//圆环是否可触摸
    private static final String INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE = "wheel_scroll_only_one_circle";//圆环是否只滚动一圈

    private Paint mWheelPaint;//圆环画笔

    private Paint mReachedPaint;//已完成进度画笔

    private Paint mReachedEdgePaint;//已完成进度两头的圆角画笔

    private Paint mPointerPaint;//指针画笔

    private int mMaxProcess;//最大进度值
    private int mCurProcess;//当前进度值
    private float mUnreachedRadius;//未完成进度半径
    private int mReachedColor, mUnreachedColor;//已完成进度颜色，未完成进度颜色
    private float mReachedWidth, mUnreachedWidth;//已完成进度宽度，未完成进度宽度
    private boolean isHasReachedCornerRound;//已完成进度是否有圆角
    private int mPointerColor;//指针颜色
    private float mPointerRadius;//指针半径

    private double mCurAngle;//当前角度
    private float mWheelCurX, mWheelCurY;//锚点圆心坐标

    private boolean isHasWheelShadow, isHasPointerShadow;//圆环是否有阴影，指针是否有阴影
    private float mWheelShadowRadius, mPointerShadowRadius;//圆环阴影半径，指针阴影半径

    private boolean isHasCache;//是否有缓存
    private Canvas mCacheCanvas;//缓存画布
    private Bitmap mCacheBitmap;//缓存Bitmap

    private boolean isCanTouch;//是否可触摸

    private boolean isScrollOneCircle;//是否只滚动一圈

    private float mDefShadowOffset;//默认阴影偏移量

    private OnSeekBarChangeListener mChangListener;

    public CircleSeekBar(Context context) {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();
    }

    private void initPaints() {
        mDefShadowOffset = getDimen(R.dimen.def_shadow_offset);
        /**
         * 圆环画笔
         */
        mWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStyle(Paint.Style.STROKE);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasWheelShadow) {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 选中区域画笔
         */
        mReachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedPaint.setColor(mReachedColor);
        mReachedPaint.setStyle(Paint.Style.STROKE);
        mReachedPaint.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        /**
         * 锚点画笔
         */
        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setColor(mPointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);
        if (isHasPointerShadow) {
            mPointerPaint.setShadowLayer(mPointerShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 选中区域两头的圆角画笔
         */
        mReachedEdgePaint = new Paint(mReachedPaint);
        mReachedEdgePaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleSeekBar, defStyle, 0);
        mMaxProcess = a.getInt(R.styleable.CircleSeekBar_wheel_max_process, 100);
        mCurProcess = a.getInt(R.styleable.CircleSeekBar_wheel_cur_process, 0);
        if (mCurProcess > mMaxProcess) mCurProcess = mMaxProcess;
        mReachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color, getColor(R.color.def_reached_color));
        mUnreachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_unreached_color,
                getColor(R.color.def_wheel_color));
        mUnreachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_unreached_width,
                getDimen(R.dimen.def_wheel_width));
        isHasReachedCornerRound = a.getBoolean(R.styleable.CircleSeekBar_wheel_reached_has_corner_round, true);
        mReachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_reached_width, mUnreachedWidth);
        mPointerColor = a.getColor(R.styleable.CircleSeekBar_wheel_pointer_color, getColor(R.color.def_pointer_color));
        mPointerRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_radius, mReachedWidth / 2);
        isHasWheelShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_wheel_shadow, false);
        if (isHasWheelShadow) {
            mWheelShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasPointerShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_pointer_shadow, false);
        if (isHasPointerShadow) {
            mPointerShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasCache = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_cache, isHasWheelShadow);
        isCanTouch = a.getBoolean(R.styleable.CircleSeekBar_wheel_can_touch, true);
        isScrollOneCircle = a.getBoolean(R.styleable.CircleSeekBar_wheel_scroll_only_one_circle, false);

        if (isHasPointerShadow | isHasWheelShadow) {
            setSoftwareLayer();
        }
        a.recycle();
    }

    private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = 0, paddingEnd = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            paddingStart = getPaddingStart();
            paddingEnd = getPaddingEnd();
        }
        int maxPadding = Math.max(paddingLeft, Math.max(paddingTop,
                Math.max(paddingRight, Math.max(paddingBottom, Math.max(paddingStart, paddingEnd)))));
        setPadding(maxPadding, maxPadding, maxPadding, maxPadding);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getContext().getColor(colorId);
        } else {
            return ContextCompat.getColor(getContext(), colorId);
        }
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    private void setSoftwareLayer() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //将 View 设置为一个正方形，边长为宽度和高度中的较小值
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        refreshPosition();//刷新锚点位置
        refreshUnreachedWidth();//刷新默认圆环半径
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 计算基本尺寸参数
        final float halfUnreachedWidth = mUnreachedWidth / 2f;
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // 计算绘制区域
        final float left = getPaddingLeft() + halfUnreachedWidth;
        final float top = getPaddingTop() + halfUnreachedWidth;
        final float right = width - getPaddingRight() - halfUnreachedWidth;
        final float bottom = height - getPaddingBottom() - halfUnreachedWidth;

        // 计算中心点
        final float centerX = (left + right) * 0.5f;
        final float centerY = (top + bottom) * 0.5f;

        // 计算半径
        final float wheelRadius = (width - getPaddingLeft() - getPaddingRight()) * 0.5f - halfUnreachedWidth;

        // 绘制背景圆环（使用缓存或实时绘制）
        //为什么需要缓存？
        //性能优化
        //减少重复计算：背景圆环的几何计算只需要一次
        //
        //避免重复绘制：静态内容只需绘制一次到 Bitmap，之后直接复用
        //
        //降低GPU负担：减少每帧需要上传到GPU的数据量
        //
        //使用场景
        //当 isHasCache 为 true 时启用缓存机制
        //
        //特别适合静态内容多、动态内容少的自定义 View
        if (isHasCache) {
            if (mCacheCanvas == null) {
                buildCache(centerX, centerY, wheelRadius);
            }
            canvas.drawBitmap(mCacheBitmap, 0, 0, null);
        } else {
            canvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint); //绘制背景圆环
        }

        // 绘制进度弧线
        canvas.drawArc(left, top, right, bottom, -90f, (float) mCurAngle, false, mReachedPaint);

        //画锚点
        canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
    }

    private void buildCache(float centerX, float centerY, float wheelRadius) {
        mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);

        //画环
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (isCanTouch && (event.getAction() == MotionEvent.ACTION_MOVE || isTouch(x, y))) {
            // 通过当前触摸点搞到cos角度值
            float cos = computeCos(x, y);
            // 通过反三角函数获得角度值

            // 在单位圆中，任意点的坐标(x,y)可以表示为(cosθ, sinθ)
            // 已知点的坐标，可以通过反余弦函数(arccos)求出角度

            // 反余弦函数返回的是弧度值，范围是[0, π]（0°~180°）
            double angle;
            if (x < getWidth() / 2) { // 滑动超过180度  触摸点在左半边（180°~360°）
                angle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
            } else { // 没有超过180度 触摸点在右半边（0°~180°）
                angle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
            }
            if (isScrollOneCircle) {
                if (mCurAngle > 270 && angle < 90) {
                    mCurAngle = 360;
                    cos = -1;// 防止跳跃
                } else if (mCurAngle < 90 && angle > 270) {
                    mCurAngle = 0;
                    cos = -1;
                } else {
                    mCurAngle = angle;
                }
            } else {
                mCurAngle = angle; // 非循环模式，直接赋值
            }
            // 根据角度计算当前进度（如 0~100）
            mCurProcess = getSelectedValue();
            // 更新滑块的坐标（圆环上的指针位置）
            refreshWheelCurPosition(cos);
            // 仅在 ACTION_MOVE 或 ACTION_UP 时触发回调。
            if (mChangListener != null && (event.getAction() & (MotionEvent.ACTION_MOVE | MotionEvent.ACTION_UP)) > 0) {
                mChangListener.onChanged(this, mCurProcess);
            }
            // 重绘
            invalidate();
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    // 根据 两点间距离公式，判断触摸点 (x, y) 到圆心 (centerX, centerY) 的距离是否小于半径 radius。
    private boolean isTouch(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        // 直接比较 平方距离 和 半径平方， 三角型 两边平方之和大于第三边平方 就不在圆内
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius;
    }

    private float getCircleWidth() {
        return Math.max(mUnreachedWidth, Math.max(mReachedWidth, mPointerRadius));
    }

    // 计算默认圆环的半径
    private void refreshUnreachedWidth() {
        mUnreachedRadius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
    }

    private void refreshWheelCurPosition(double cos) {
        mWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mWheelCurY = calcYLocationInWheel(cos);
    }

    private void refreshPosition() {
        //计算当前角度
        mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
        //Math.toRadians(mCurAngle) 将角度 mCurAngle 从度数转换为弧度。因为在 Java 的 Math 库中，三角函数（如 cos 方法）使用的是弧度制
        double cos = -Math.cos(Math.toRadians(mCurAngle));
        refreshWheelCurPosition(cos);
    }

    // 计算圆上某点的X坐标
    private float calcXLocationInWheel(double angle, double cos) {
        // Math.sqrt(1 - cos * cos) 根据三角函数关系，对于给定角度的余弦值 cos，通过 sin²α + cos²α = 1，
        // 这里计算出正弦值（sin = Math.sqrt(1 - cos * cos)）
        if (angle < 180) {
            return (float) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * mUnreachedRadius);
        } else {
            return (float) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * mUnreachedRadius);
        }
    }

    // 计算圆上某点的Y坐标
    // 因为cos 传进来的本来就是负值，所以这里直接用cos 计算，
    // 补充一下自行脑补余弦图：（0度 至 90度）cos 是正值，（90度 至 180度）cos 是负值，（180度 至 270度）cos 是负值，（270度 至 360度）cos 是正值
    private float calcYLocationInWheel(double cos) {
        return getMeasuredWidth() / 2 + mUnreachedRadius * (float) cos;
    }

    /**
     * 拿到倾斜的cos值
     */
    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INATANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_MAX_PROCESS, mMaxProcess);
        bundle.putInt(INSTANCE_CUR_PROCESS, mCurProcess);
        bundle.putInt(INSTANCE_REACHED_COLOR, mReachedColor);
        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);
        bundle.putBoolean(INSTANCE_REACHED_CORNER_ROUND, isHasReachedCornerRound);
        bundle.putInt(INSTANCE_UNREACHED_COLOR, mUnreachedColor);
        bundle.putFloat(INSTANCE_UNREACHED_WIDTH, mUnreachedWidth);
        bundle.putInt(INSTANCE_POINTER_COLOR, mPointerColor);
        bundle.putFloat(INSTANCE_POINTER_RADIUS, mPointerRadius);
        bundle.putBoolean(INSTANCE_POINTER_SHADOW, isHasPointerShadow);
        bundle.putFloat(INSTANCE_POINTER_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_SHADOW, isHasWheelShadow);
        bundle.putFloat(INSTANCE_WHEEL_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_HAS_CACHE, isHasCache);
        bundle.putBoolean(INSTANCE_WHEEL_CAN_TOUCH, isCanTouch);
        bundle.putBoolean(INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE, isScrollOneCircle);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INATANCE_STATE));
            mMaxProcess = bundle.getInt(INSTANCE_MAX_PROCESS);
            mCurProcess = bundle.getInt(INSTANCE_CUR_PROCESS);
            mReachedColor = bundle.getInt(INSTANCE_REACHED_COLOR);
            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH);
            isHasReachedCornerRound = bundle.getBoolean(INSTANCE_REACHED_CORNER_ROUND);
            mUnreachedColor = bundle.getInt(INSTANCE_UNREACHED_COLOR);
            mUnreachedWidth = bundle.getFloat(INSTANCE_UNREACHED_WIDTH);
            mPointerColor = bundle.getInt(INSTANCE_POINTER_COLOR);
            mPointerRadius = bundle.getFloat(INSTANCE_POINTER_RADIUS);
            isHasPointerShadow = bundle.getBoolean(INSTANCE_POINTER_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_POINTER_SHADOW_RADIUS);
            isHasWheelShadow = bundle.getBoolean(INSTANCE_WHEEL_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_WHEEL_SHADOW_RADIUS);
            isHasCache = bundle.getBoolean(INSTANCE_WHEEL_HAS_CACHE);
            isCanTouch = bundle.getBoolean(INSTANCE_WHEEL_CAN_TOUCH);
            isScrollOneCircle = bundle.getBoolean(INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE);
            initPaints();
        } else {
            super.onRestoreInstanceState(state);
        }

        if (mChangListener != null) {
            mChangListener.onChanged(this, mCurProcess);
        }
    }

    private int getSelectedValue() {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    public int getCurProcess() {
        return mCurProcess;
    }

    public void setCurProcess(int curProcess) {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        if (mChangListener != null) {
            mChangListener.onChanged(this, curProcess);
        }
        refreshPosition();
        invalidate();
    }

    public int getMaxProcess() {
        return mMaxProcess;
    }

    public void setMaxProcess(int maxProcess) {
        mMaxProcess = maxProcess;
        refreshPosition();
        invalidate();
    }

    public int getReachedColor() {
        return mReachedColor;
    }

    public void setReachedColor(int reachedColor) {
        this.mReachedColor = reachedColor;
        mReachedPaint.setColor(reachedColor);
        mReachedEdgePaint.setColor(reachedColor);
        invalidate();
    }

    public int getUnreachedColor() {
        return mUnreachedColor;
    }

    public void setUnreachedColor(int unreachedColor) {
        this.mUnreachedColor = unreachedColor;
        mWheelPaint.setColor(unreachedColor);
        invalidate();
    }

    public float getReachedWidth() {
        return mReachedWidth;
    }

    public void setReachedWidth(float reachedWidth) {
        this.mReachedWidth = reachedWidth;
        mReachedPaint.setStrokeWidth(reachedWidth);
        mReachedEdgePaint.setStrokeWidth(reachedWidth);
        invalidate();
    }

    public boolean isHasReachedCornerRound() {
        return isHasReachedCornerRound;
    }

    public void setHasReachedCornerRound(boolean hasReachedCornerRound) {
        isHasReachedCornerRound = hasReachedCornerRound;
        mReachedPaint.setStrokeCap(hasReachedCornerRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        invalidate();
    }

    public float getUnreachedWidth() {
        return mUnreachedWidth;
    }

    public void setUnreachedWidth(float unreachedWidth) {
        this.mUnreachedWidth = unreachedWidth;
        mWheelPaint.setStrokeWidth(unreachedWidth);
        refreshUnreachedWidth();
        invalidate();
    }

    public int getPointerColor() {
        return mPointerColor;
    }

    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
        mPointerPaint.setColor(pointerColor);
    }

    public float getPointerRadius() {
        return mPointerRadius;
    }

    public void setPointerRadius(float pointerRadius) {
        this.mPointerRadius = pointerRadius;
        mPointerPaint.setStrokeWidth(pointerRadius);
        invalidate();
    }

    public boolean isHasWheelShadow() {
        return isHasWheelShadow;
    }

    public void setWheelShadow(float wheelShadow) {
        this.mWheelShadowRadius = wheelShadow;
        if (wheelShadow == 0) {
            isHasWheelShadow = false;
            mWheelPaint.clearShadowLayer();
            mCacheCanvas = null;
            mCacheBitmap.recycle();
            mCacheBitmap = null;
        } else {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public float getWheelShadowRadius() {
        return mWheelShadowRadius;
    }

    public boolean isHasPointerShadow() {
        return isHasPointerShadow;
    }

    public float getPointerShadowRadius() {
        return mPointerShadowRadius;
    }

    public void setPointerShadowRadius(float pointerShadowRadius) {
        this.mPointerShadowRadius = pointerShadowRadius;
        if (mPointerShadowRadius == 0) {
            isHasPointerShadow = false;
            mPointerPaint.clearShadowLayer();
        } else {
            mPointerPaint.setShadowLayer(pointerShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mChangListener = listener;
    }

    public interface OnSeekBarChangeListener {
        void onChanged(CircleSeekBar seekbar, int curValue);
    }
}
