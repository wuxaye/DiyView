package com.xaye.diyview.netease.paint.xfermode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * 图像混合 —— 橡皮擦刮奖（PorterDuff.Mode.SRC_OUT）。
 * （原外部 Demo 的 XfermodeActivity，直接 setContentView(new XfermodeEraserView)，
 * 这里改为布局 + 统一标题栏/返回键）
 */
public class XfermodeEraserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode_eraser);

        // 统一标题栏：标题取清单 label，返回键 finish
        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
