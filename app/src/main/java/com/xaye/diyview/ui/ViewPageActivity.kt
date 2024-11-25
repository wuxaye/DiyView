package com.xaye.diyview.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.quickdev.test.viewpage.ViewPagerAdapter
import com.xaye.diyview.app.base.BaseActivity
import com.xaye.diyview.databinding.ActivityViewPageBinding
import com.xaye.diyview.view.tv.ColorTrackTextView
import com.xaye.helper.base.BaseViewModel

class ViewPageActivity : BaseActivity<BaseViewModel, ActivityViewPageBinding>() {

    private val items: List<String> by lazy {
        listOf("首页", "关注", "附近", "我的")
    }

    private val mIndicators: ArrayList<ColorTrackTextView> by lazy { ArrayList() }
    override fun initView(savedInstanceState: Bundle?) {
        initIndicator()
        initViewPager()
    }

    private fun initIndicator() {
        items.forEachIndexed { index, s ->
            //动态添加颜色跟踪的TextView
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1f
            val colorTrackTextView = ColorTrackTextView(this)

            //设置颜色
            colorTrackTextView.setChangeColor(Color.RED)
            colorTrackTextView.setOriginColor(Color.BLACK)
            colorTrackTextView.textSize = 20f
            colorTrackTextView.text = s
            colorTrackTextView.layoutParams = params

            // 把新的加入LinearLayout 中
            mBind.indicator.addView(colorTrackTextView)
            //加入集合
            mIndicators.add(colorTrackTextView)
        }

    }

    private fun initViewPager() {
        mBind.viewPager.adapter = ViewPagerAdapter(this)

        mBind.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // 页面滚动时调用
                // position：当前页面的位置
                // positionOffset：滑动的进度（0到1之间）
                // positionOffsetPixels：滑动的像素
                Log.i(
                    "TAG",
                    "position:$position positionOffset:$positionOffset positionOffsetPixels:$positionOffsetPixels"
                )

                val left = mIndicators.getOrNull(position)
                val right = mIndicators.getOrNull(position + 1)

                if (left != null) {
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
                    left.setProgress(1 - positionOffset)
                }

                if (right != null) {
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT)
                    right.setProgress(positionOffset)
                }


            }
        })
    }

}