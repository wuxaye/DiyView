package com.xaye.diyview.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import com.xaye.diyview.R
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityTrackViewBinding
import com.xaye.helper.base.BaseViewModel

class TrackViewActivity : BaseActivity<BaseViewModel, ActivityTrackViewBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        // 添加三个用户，设置不同的初始速度和加速度
        mBind.trackView.addUser(BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar), 2f, 0.05f);
        mBind.trackView.addUser(BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar), 3f, -0.02f);
        mBind.trackView.addUser(BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar), 1f, 0.03f);

        startSimulation();
    }

    private fun startSimulation() {
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                mBind.trackView.updateUserDistance()
                handler.postDelayed(this, 33) // 每 16 毫秒更新 (约 60 FPS)
            }
        }
        handler.post(runnable)
    }

}