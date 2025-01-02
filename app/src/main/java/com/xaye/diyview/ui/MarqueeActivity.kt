package com.xaye.diyview.ui

import android.graphics.Color
import android.os.Bundle
import com.xaye.diyview.R
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityMarqueeBinding
import com.xaye.diyview.view.marquee.MarqueeView
import com.xaye.helper.base.BaseViewModel


class MarqueeActivity : BaseActivity<BaseViewModel, ActivityMarqueeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mBind.marqueeView.apply {
            setTextList(mutableListOf("第一段文本11111111111111111111111", "第二段文本2222222", "第三段文本"))
            setTextColor(Color.BLUE)
            setTextSize(20f)
            setSpeed(MarqueeView.SPEED_SLOW)
            setBackgroundColor(Color.LTGRAY)
            setCornerRadius(20f)
            setDirection(MarqueeView.DIRECTION_RIGHT_TO_LEFT)
            setIcon(R.drawable.star_selected, MarqueeView.ICON_POSITION_LEFT)
            startScroll()
        }

    }

}