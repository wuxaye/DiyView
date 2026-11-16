package com.xaye.diyview.netease.canvas.transform;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xaye.diyview.R;

/**
 * Canvas 状态保存与恢复 —— save/restore/restoreToCount/saveLayer 示例。
 */
public class CanvasSaveRestoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_save_restore);

        ((TextView) findViewById(R.id.tv_title_bar)).setText(getTitle());
        findViewById(R.id.iv_title_bar_back).setOnClickListener(v -> finish());
    }
}
