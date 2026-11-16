package com.xaye.diyview.netease.paint.colorfilter;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * 颜色滤镜 —— 自定义 View 绘制示例。
 */
public class ColorFilterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_filter_view);

        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
