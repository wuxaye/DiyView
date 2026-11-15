package com.xaye.diyview.netease.paint.xfermode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * 图像混合 —— 18 种 PorterDuff 模式总览（源自 Google APIDemo）。
 * （原外部 Demo 把 SampleView 作为内部类写在 Activity 里，这里抽成独立的 XfermodesView）
 */
public class XfermodesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermodes);

        // 统一标题栏：标题取清单 label，返回键 finish
        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
