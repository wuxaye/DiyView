package com.xaye.diyview.netease.canvas.split;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * 粒子效果 —— 图片按像素点碎裂散落（轻触屏幕开始动画）。
 */
public class CanvasSplitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_split);

        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
