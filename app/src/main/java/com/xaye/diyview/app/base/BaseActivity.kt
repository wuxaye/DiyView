package com.xaye.diyview.app.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.xaye.diyview.R
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

    //统一标题栏（带返回键），复用框架自带的 getTitleBarView/showToolBar 机制
    private var titleBarView: View? = null

    override fun initImmersionBar() {
        //设置共同沉浸式样式
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    /**
     * 是否显示统一标题栏（带返回键），默认显示；
     * 主页等根页面可重写返回 false 隐藏标题栏
     */
    override fun showToolBar(): Boolean = true

    /**
     * 标题栏文案，默认取清单文件中当前 Activity 的 android:label
     */
    open fun titleBarTitle(): CharSequence =
        runCatching { packageManager.getActivityInfo(componentName, 0).loadLabel(packageManager) }.getOrElse { "" }

    /**
     * 提供统一的标题栏视图：左侧返回键点击后 finish()，中间显示标题
     */
    override fun getTitleBarView(): View? {
        if (!showToolBar()) return null
        if (titleBarView == null) {
            titleBarView = layoutInflater.inflate(R.layout.layout_title_bar, null)
            titleBarView!!.findViewById<TextView>(R.id.tv_title_bar).text = titleBarTitle()
            titleBarView!!.findViewById<View>(R.id.iv_title_bar_back).setOnClickListener {
                finish()
            }
        }
        return titleBarView
    }
}