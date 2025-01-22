package com.xaye.diyview.ui

import android.os.Bundle
import com.hjq.toast.ToastUtils
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityEraseBinding
import com.xaye.helper.base.BaseViewModel

class EraseActivity : BaseActivity<BaseViewModel, ActivityEraseBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mBind.eraseView.setOnEraseCompleteListener { result ->
            ToastUtils.show(result)
        }
    }

}