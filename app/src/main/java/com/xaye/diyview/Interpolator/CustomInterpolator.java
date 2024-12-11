package com.xaye.diyview.Interpolator;

import android.view.animation.Interpolator;

public class CustomInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        // 使用正弦函数创建一个波动效果
        return (float) Math.sin(input * Math.PI);  // 从 0 到 1，再回到 0
    }
}
