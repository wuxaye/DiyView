package com.xaye.diyview.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import com.xaye.diyview.Interpolator.SpringInterpolator
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityRatingBarBinding
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.clickNoRepeat


class RatingBarActivity : BaseActivity<BaseViewModel, ActivityRatingBarBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mBind.btnClick.clickNoRepeat {


            // 使用弹簧插值器
            val animator = ObjectAnimator.ofFloat(mBind.ratingView, "translationY", 0f, -300f)
            animator.setDuration(1000)
            animator.interpolator = SpringInterpolator()
            animator.start()

        }
    }

}