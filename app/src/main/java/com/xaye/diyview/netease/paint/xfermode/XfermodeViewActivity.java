package com.xaye.diyview.netease.paint.xfermode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * 图像混合 —— 基础示例（PorterDuff.Mode.DST_IN）。
 */
public class XfermodeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode_view);

        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
