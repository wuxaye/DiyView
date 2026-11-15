package com.xaye.diyview.netease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityNeteaseMenuBinding
import com.xaye.helper.base.BaseViewModel
import com.xaye.helper.ext.clickNoRepeat

/**
 * UI 示例合集的两级菜单入口。
 *
 * - 未携带 [EXTRA_GROUP_TITLE]：一级，展示 [NeteaseRegistry.groups] 各分组标题；
 * - 携带分组标题：二级，展示该分组下的示例，点击跳转到对应 Activity。
 *
 * 通过同一 Activity 复用两级渲染，标题栏返回键天然支持从二级回到一级、一级回到主页。
 */
class NeteaseMenuActivity : BaseActivity<BaseViewModel, ActivityNeteaseMenuBinding>() {

    companion object {
        const val EXTRA_GROUP_TITLE = "extra_group_title"
        private const val MENU_TITLE = "UI示例合集"
    }

    private val groupTitle: String?
        get() = intent.getStringExtra(EXTRA_GROUP_TITLE)

    override fun titleBarTitle(): CharSequence = groupTitle ?: MENU_TITLE

    override fun initView(savedInstanceState: Bundle?) {
        val container = mBind.container
        container.removeAllViews()
        val group = groupTitle?.let { NeteaseRegistry.findGroup(it) }
        if (group == null) {
            // 一级：功能分组
            NeteaseRegistry.groups.forEach { g ->
                container.addView(menuButton(g.title) {
                    startActivity(
                        Intent(this, NeteaseMenuActivity::class.java)
                            .putExtra(EXTRA_GROUP_TITLE, g.title)
                    )
                })
            }
        } else {
            // 二级：分组下的示例
            group.demos.forEach { demo ->
                container.addView(menuButton(demo.title) {
                    startActivity(Intent(this, demo.target))
                })
            }
        }
    }

    private fun menuButton(text: String, onClick: () -> Unit): Button {
        return Button(this).apply {
            this.text = text
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            clickNoRepeat { onClick() }
        }
    }

    override fun onBindViewClick() {
        // 动态按钮点击已在 initView 内逐个绑定
    }
}
