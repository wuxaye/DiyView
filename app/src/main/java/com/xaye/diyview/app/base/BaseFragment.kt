package com.xaye.diyview.app.base

import androidx.viewbinding.ViewBinding
import com.xaye.helper.base.BaseVbFragment
import com.xaye.helper.base.BaseViewModel

/**
 * Author xaye
 * @date: 2024-06-13 14:26
 *
 * 需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看
 */
abstract class BaseFragment<VM : BaseViewModel,VB: ViewBinding> : BaseVbFragment<VM, VB>() {
}