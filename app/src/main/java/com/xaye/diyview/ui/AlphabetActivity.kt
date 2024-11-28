package com.xaye.diyview.ui

import android.os.Bundle
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityAlphabetBinding
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.gone
import com.xaye.helper.ext.visible

class AlphabetActivity : BaseActivity<BaseViewModel, ActivityAlphabetBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mBind.atView.setOnLetterSelectedListener { letter,isTouch ->
            if (isTouch) {
                mBind.tvLetter.visible()
            } else {
                mBind.tvLetter.postDelayed({
                    mBind.tvLetter.gone()
                }, 500)
            }
            mBind.tvLetter.text = letter
        }
    }

}