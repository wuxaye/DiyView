package com.xaye.diyview.netease.paint.colorfilter;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * 颜色滤镜 —— 自定义 View 绘制示例。
 * （原外部 Demo 的 MainActivity，重命名为语义化名称，并接入统一标题栏/返回键）
 */
public class ColorFilterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_filter_view);

        // 统一标题栏：标题取清单 label，返回键 finish
        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
