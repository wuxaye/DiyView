package com.xaye.diyview.ui

import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.xaye.diyview.R
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityProgressExBinding
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.clickNoRepeat

class ProgressExActivity : BaseActivity<BaseViewModel, ActivityProgressExBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        mBind.circularProgressBar.setPointerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point))
        mBind.circularProgressBar2.setPointerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point))
        mBind.circularProgressBar3.setPointerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point))
        mBind.circularProgressBar4.setPointerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point))
        mBind.btnStartAnimation.clickNoRepeat {
            val animator = ValueAnimator.ofInt(0, 95)
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