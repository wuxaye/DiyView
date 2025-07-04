package com.xaye.diyview

import android.content.Intent
import android.os.Bundle
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityMainBinding
import com.xaye.diyview.ui.AlphabetActivity
import com.xaye.diyview.ui.CircleSeekBarActivity
import com.xaye.diyview.ui.EraseActivity
import com.xaye.diyview.ui.LoadingActivity
import com.xaye.diyview.ui.MarqueeActivity
import com.xaye.diyview.ui.ProgressActivity
import com.xaye.diyview.ui.ProgressExActivity
import com.xaye.diyview.ui.QQStepActivity
import com.xaye.diyview.ui.RatingBarActivity
import com.xaye.diyview.ui.StepViewActivity
import com.xaye.diyview.ui.TrackActivity
import com.xaye.diyview.ui.TrackViewActivity
import com.xaye.diyview.ui.ViewPageActivity
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.setOnclickNoRepeat

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        setOnclickNoRepeat(
            mBind.btn58view,
            mBind.btnTvTrack,
            mBind.btnCircleProgress,
            mBind.btnCircleProgressEx,
            mBind.btnQqstep,
            mBind.btnTrackTvVp,
            mBind.btnRating,
            mBind.btnAlphabet,
            mBind.btnTrack,
            mBind.btnMarquee,
            mBind.btnErase,
            mBind.btnCircleSeekBar,
            mBind.btnStepView
        ) {
            when (it.id) {
                R.id.btn_58view -> {
                    startActivity(Intent(this, LoadingActivity::class.java))
                }

                R.id.btn_tv_track -> {
                    startActivity(Intent(this, TrackActivity::class.java))
                }

                R.id.btn_circle_progress -> {
                    startActivity(Intent(this, ProgressActivity::class.java))
                }

                R.id.btn_circle_progress_ex -> {
                    startActivity(Intent(this, ProgressExActivity::class.java))
                }

                R.id.btn_qqstep -> {
                    startActivity(Intent(this, QQStepActivity::class.java))
                }

                R.id.btn_track_tv_vp -> {
                    startActivity(Intent(this, ViewPageActivity::class.java))
                }

                R.id.btn_rating -> {
                    startActivity(Intent(this, RatingBarActivity::class.java))
                }

                R.id.btn_alphabet -> {
                    startActivity(Intent(this, AlphabetActivity::class.java))
                }

                R.id.btn_track -> {
                    startActivity(Intent(this, TrackViewActivity::class.java))
                }

                R.id.btn_marquee -> {
                    startActivity(Intent(this, MarqueeActivity::class.java))
                }

                R.id.btn_erase -> {
                    startActivity(Intent(this, EraseActivity::class.java))
                }

                R.id.btn_circle_seek_bar -> {
                    startActivity(Intent(this, CircleSeekBarActivity::class.java))
                }
                R.id.btn_step_view -> {
                    startActivity(Intent(this, StepViewActivity::class.java))
                }
            }
        }
    }

}