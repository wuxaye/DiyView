package com.xaye.diyview.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.xaye.diyview.R
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityTrackViewBinding
import com.xaye.helper.base.BaseViewModel

class TrackViewActivity : BaseActivity<BaseViewModel, ActivityTrackViewBinding>() {

    private val handler = Handler()
    override fun initView(savedInstanceState: Bundle?) {
        // 添加三个用户，设置不同的初始速度和加速度
        mBind.trackView.addUser(BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar))
        mBind.trackView.addUser(BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar))
        mBind.trackView.addUser(BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar))

        // 启动 TrackView
        mBind.trackView.start();

        startSimulation();
    }

    private fun startSimulation() {
        var oDis1 = 0f
        var oDis2 = 0f
        var oDis3 = 0f

        val runnable: Runnable = object : Runnable {
            override fun run() {
                // 模拟服务器推送的距离
                Log.i("TAG", "oDis1:$oDis1,oDis2:$oDis2,oDis3:$oDis3")
                mBind.trackView.updateUserDistance(floatArrayOf(oDis1, oDis2, oDis3))
                handler.postDelayed(this, 1000) // 每隔 1 秒更新

                oDis2+=8
                oDis3+=7

                if (oDis1 < 100f) {
                    oDis1+=5
                }
            }
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBind.trackView.stop(); // 确保 View 停止刷新，避免内存泄漏
        handler.removeCallbacksAndMessages(null);
    }

}