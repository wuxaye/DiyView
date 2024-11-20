package com.xaye.diyview.ui

import android.animation.ValueAnimator
import android.os.Bundle
import com.xaye.diyview.R
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityTrackBinding
import com.xaye.diyview.view.tv.ColorTrackTextView
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.setOnclickNoRepeat

class TrackActivity : BaseActivity<BaseViewModel, ActivityTrackBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        setOnclickNoRepeat(mBind.btnLeftToRight, mBind.btnRightToLeft) {
            when (it.id) {
                R.id.btn_left_to_right -> leftToRight()
                R.id.btn_right_to_left -> rightToLeft()
            }
        }
    }

    private fun leftToRight() {
        val trackTextView = mBind.tvTrack
        trackTextView.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT)
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(2000)
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            trackTextView.setProgress(value)
        }
        valueAnimator.start()
    }

    private fun rightToLeft() {
        val trackTextView = mBind.tvTrack
        trackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(2000)
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            trackTextView.setProgress(value)
        }
        valueAnimator.start()
    }
}