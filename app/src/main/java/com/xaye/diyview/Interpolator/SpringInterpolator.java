package com.xaye.diyview.Interpolator;

import android.view.animation.Interpolator;

public class SpringInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        // 创建一个弹簧效果
        return (float) (-Math.pow(2, -10 * input) * Math.sin((input - 0.3f) * 2 * Math.PI / 0.3f) + 1);
    }
}
