package com.xaye.diyview.view.loading;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;

public class LoadingView extends LinearLayout {
    private ShapeView mShapeView;
    private View mShadowView;

    private int mTranslationY = 0;  // 当前弹跳偏移量
    private final long ANIMATOR_DURATION = 350; // 动画时长

    private AnimatorSet mCurrentAnimatorSet; // 当前执行的 AnimatorSet

    private Animator rotationAnimator;

    private boolean canceled = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTranslationY = dip2px(80);
        initLayout();
    }

    private int dip2px(int dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, getResources().getDisplayMetrics());
    }

    //初始化加载布局
    private void initLayout() {
        // 添加到该View
        inflate(getContext(), R.layout.layout_loading_view, this);

        mShapeView = findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);

        startFalling();
    }

    // 开始下落动画
    private void startFalling() {
        // 下落位移动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationY);
        animator.setDuration(ANIMATOR_DURATION);

        //配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1, 0.3f);
        scaleAnimator.setDuration(ANIMATOR_DURATION);

        // 动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        // 加速
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(animator, scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!canceled) {
                    // 改变形状
                    mShapeView.exchangeShape();
                    // 下落完动画，开始上升动画
                    startRising();
                }
            }
        });

        // 如果已有动画在执行，取消之前的动画
        if (mCurrentAnimatorSet != null && mCurrentAnimatorSet.isRunning()) {
            mCurrentAnimatorSet.cancel();
        }
        mCurrentAnimatorSet = animatorSet;
        animatorSet.start();
    }

    private void startRising() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationY, 0);
        animator.setDuration(ANIMATOR_DURATION);

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1);
        scaleAnimator.setDuration(ANIMATOR_DURATION);

        AnimatorSet animatorSet = new AnimatorSet();
        // 减速
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(animator, scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!canceled) {
                    startFalling();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                //开始旋转
                startRotationAnimator();
                Log.d("LoadingView", "onAnimationStart: " + mShapeView.getCurrentShapeType());
            }
        });

        // 如果已有动画在执行，取消之前的动画
        if (mCurrentAnimatorSet != null && mCurrentAnimatorSet.isRunning()) {
            mCurrentAnimatorSet.cancel();
        }

        mCurrentAnimatorSet = animatorSet;
        animatorSet.start();
    }

    private void startRotationAnimator() {
        switch (mShapeView.getCurrentShapeType()) {
            case CIRCLE:
            case SQUARE:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
            case TRIANGLE:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, -60);
                break;
        }
        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.start();
    }

    // 停止所有动画的方法
    public void stopAnimation() {

        // 取消当前的动画集合
        if (mCurrentAnimatorSet != null && mCurrentAnimatorSet.isRunning()) {
            Log.d("LoadingView", "Cancelling current AnimatorSet");
            mCurrentAnimatorSet.cancel();
        }

        // 取消旋转动画
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            Log.d("LoadingView", "Cancelling rotationAnimator");
            rotationAnimator.cancel();
        }

        canceled = true;

        mCurrentAnimatorSet = null;
        rotationAnimator = null;
    }

}
