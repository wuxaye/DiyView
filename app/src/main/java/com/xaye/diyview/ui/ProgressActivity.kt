package com.xaye.diyview.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityProgressBinding
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.clickNoRepeat

class ProgressActivity : BaseActivity<BaseViewModel, ActivityProgressBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        mBind.btnStartAnimation.clickNoRepeat {
            val animator = ValueAnimator.ofInt(0, 80)
            animator.setDuration(2000)
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                mBind.circularProgressBar.setProgress(value)
                mBind.circularProgressBar2.progress = value
                mBind.circularProgressBar3.progress = value
                mBind.circularProgressBar4.progress = value
            }

            animator.start()
        }
    }

}