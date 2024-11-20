package com.xaye.diyview.app.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.xaye.helper.base.BaseVBActivity
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.base.action.BundleAction
import com.xaye.helper.base.action.HandlerAction

/**
 * Author xaye
 * @date: 2024-06-13 14:15
 *
 * 需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVBActivity<VM, VB>(),
    BundleAction, HandlerAction {

    override fun initImmersionBar() {
        //设置共同沉浸式样式
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }
}