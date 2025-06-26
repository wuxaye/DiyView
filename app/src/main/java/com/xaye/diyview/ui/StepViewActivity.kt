package com.xaye.diyview.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityStepViewBinding
import com.xaye.diyview.view.stepview.data.StepItemData
import com.xaye.helper.base.BaseViewModel
import java.util.regex.Pattern


class StepViewActivity : BaseActivity<BaseViewModel, ActivityStepViewBinding>() {

    companion object {
        const val PATTERN_PHONE: String = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}"

        const val SCHEME_TEL: String = "tel:"
    }

    var datas: MutableList<StepItemData> = ArrayList()
    override fun initView(savedInstanceState: Bundle?) {
        // 指定数据集合
        initData()
        mBind.stepView.setDatas(datas)
        // 设置view的绑定监听
        mBind.stepView.setBindViewListener { itemMsg, itemDate, data ->
            val sid = data as StepItemData
            itemMsg.text = formatPhoneNumber(itemMsg, sid.msg)
            itemDate.text = sid.date
        }
    }

    override fun initData() {
        for (i in 0..10) {
            val data = StepItemData()
            if (i % 2 == 0) {
                data.msg = "[北京市] 包裹已到达,北京市朝阳区 \n 联系电话:15912345678 "
            } else {
                data.msg = "[杭州市] 包裹已派发至转运中心,转运中心已发出。"
            }
            data.date = "2025年06月26日"
            datas.add(data)
        }
    }

    /**
     * 格式化TextView的显示格式，识别手机号
     *
     * @param textView
     * @param source
     * @return
     */
    private fun formatPhoneNumber(textView: TextView, source: String): SpannableStringBuilder {
        // 若要部分 SpannableString 可点击，需要如下设置
        textView.movementMethod = LinkMovementMethod.getInstance()
        // 将要格式化的 String 构建成一个 SpannableStringBuilder
        val value = SpannableStringBuilder(source)

        // 使用正则表达式匹配电话
        Linkify.addLinks(value, Pattern.compile(PATTERN_PHONE), SCHEME_TEL)

        // 获取上面到所有 addLinks 后的匹配部分(这里一个匹配项被封装成了一个 URLSpan 对象)
        val urlSpans = value.getSpans(0, value.length, URLSpan::class.java)
        for (urlSpan in urlSpans) {
            if (urlSpan.url.startsWith(SCHEME_TEL)) {
                val start = value.getSpanStart(urlSpan)
                val end = value.getSpanEnd(urlSpan)
                value.removeSpan(urlSpan)
                value.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val phone = urlSpan.url.replace(SCHEME_TEL, "")
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@StepViewActivity)
                        builder.setMessage("是否拨打电话：$phone")
                        builder.setNegativeButton("取消", null)
                        builder.setPositiveButton("确定", null)
                        builder.create().show()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = Color.parseColor("#3f8de2")
                        ds.isUnderlineText = true
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return value
    }

}