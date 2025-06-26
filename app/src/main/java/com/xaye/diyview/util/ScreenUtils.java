package com.xaye.diyview.util;

import android.content.Context;

/**
 * @Author: xaye
 * @CreateDate: 2025/6/26 22:07
 * @UpdateDate: 2025/6/26 22:07
 */
public class ScreenUtils {
    public static int dp2px(Context context, int dpValue) {
        int density = (int) context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
