package com.xaye.diyview.ui

import android.graphics.Color
import android.os.Bundle
import com.hjq.toast.ToastUtils
import com.xaye.diyview.R
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityMarqueeBinding
import com.xaye.diyview.view.marquee.ScrollTextView
import com.xaye.helper.base.BaseViewModel


class MarqueeActivity : BaseActivity<BaseViewModel, ActivityMarqueeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

//        mBind.marqueeView.apply {
//            setTextList(mutableListOf("第一段文本11111111111111111111111", "第二段文本2222222", "第三段文本"))
//            setTextColor(Color.BLUE)
//            setTextSize(20f)
//            setSpeed(MarqueeView.SPEED_SLOW)
//            setBackgroundColor(Color.LTGRAY)
//            setCornerRadius(20f)
//            setDirection(MarqueeView.DIRECTION_RIGHT_TO_LEFT)
//            setIcon(R.drawable.star_selected, MarqueeView.ICON_POSITION_LEFT)
//            startScroll()
//        }

        // 文字列表
        val textList = mutableListOf(
            "关于开展2025年市直公益性岗位征集工作的通知",
            "国家发通知要求规范OTA升级 广汽将构建全域智行安全守护体系",
            "2月28日，工信部联合市场监管总局发布了《关于进一步加强智能网联汽车准入、召回及软件在线升级管理的通知》（以下简称《通知》），为汽车产业健康有序发展提供了顶层指引。对此，广汽集团表示，承诺所有安全投入绝不转嫁用户成本。"
        )

        // 横向
        mBind.scrollTextH.apply {
            setTextList(textList)
            this.isHorizontal = true
            speed = 4
            textColor = Color.WHITE
            textBackColor = R.color.teal_200
            isClickEnable = true
            setOnTextClickListener { index, text ->
                ToastUtils.show("点击了 $index $text")
            }
        }

        // 竖向
        mBind.scrollTextV.apply {
            setTextList(textList) // 文字列表
            this.isHorizontal = false // 是否横向滚动
            speed = 4 // 滚动速度
            stayTimes = 3000 // 停留时间 3s
            textColor = Color.WHITE // 文字颜色
            textBackColor = R.color.teal_700 // 文字背景颜色
            isClickEnable = false // 是否可点击
        }


    }

}