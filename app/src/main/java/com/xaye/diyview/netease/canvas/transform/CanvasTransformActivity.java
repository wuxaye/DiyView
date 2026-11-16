package com.xaye.diyview.netease.canvas.transform;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * Canvas 变换 —— 平移/缩放/旋转/倾斜/切割/矩阵示例。
 */
public class CanvasTransformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_transform);

        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
