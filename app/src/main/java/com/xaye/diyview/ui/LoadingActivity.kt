package com.xaye.diyview.ui

import android.os.Bundle
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityLoadingBinding
import com.xaye.helper.base.BaseViewModel

class LoadingActivity : BaseActivity<BaseViewModel, ActivityLoadingBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
    }


    override fun onStop() {
        super.onStop()
        mBind.loadingView.stopAnimation()
    }

}