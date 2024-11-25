package com.example.quickdev.test.viewpage

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xaye.diyview.view.trackTvViewpage.FragmentFour
import com.xaye.diyview.view.trackTvViewpage.FragmentOne
import com.xaye.diyview.view.trackTvViewpage.FragmentThree
import com.xaye.diyview.view.trackTvViewpage.FragmentTwo

class ViewPagerAdapter(fragment: AppCompatActivity) : FragmentStateAdapter(fragment) {

    // 返回页面数
    override fun getItemCount(): Int = 4

    // 根据位置返回不同的 Fragment
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentOne()
            1 -> FragmentTwo()
            2 -> FragmentThree()
            3 -> FragmentFour()
            else -> FragmentOne()
        }
    }
}