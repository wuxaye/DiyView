package com.xaye.diyview.ui

import android.animation.ValueAnimator
import android.os.Bundle
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityQqstepBinding
import com.xaye.helper.base.BaseViewModel

class QQStepActivity : BaseActivity<BaseViewModel, ActivityQqstepBinding>() {

    private val valueAnimator: ValueAnimator by lazy {
        ValueAnimator.ofInt(0, 3000)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBind.stepView.setStepMax(5000)

        valueAnimator.setDuration(1000)
        valueAnimator.interpolator = android.view.animation.DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            mBind.stepView.setStep(value)
        }
    }

    override fun onBindViewClick() {
        mBind.btnStart.setOnClickListener {

            valueAnimator.start()
        }
    }

}