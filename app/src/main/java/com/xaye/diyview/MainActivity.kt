package com.xaye.diyview

import android.content.Intent
import android.os.Bundle
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityMainBinding
import com.xaye.diyview.ui.LoadingActivity
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.setOnclickNoRepeat

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        setOnclickNoRepeat(mBind.btn58view) {
            when (it.id) {
                R.id.btn_58view -> {
                    startActivity(Intent(this,LoadingActivity::class.java))
                }
            }
        }
    }

}