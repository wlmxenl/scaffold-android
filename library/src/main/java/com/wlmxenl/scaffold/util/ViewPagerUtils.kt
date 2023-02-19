package com.wlmxenl.scaffold.util

import androidx.viewpager.widget.ViewPager

object ViewPagerUtils {

    @JvmStatic
    fun setDefaultItemIndex(viewPager: ViewPager, index: Int) {
        try {
            val clazz = Class.forName("androidx.viewpager.widget.ViewPager")
            val field = clazz.getDeclaredField("mCurItem")
            field.isAccessible = true
            field.setInt(viewPager, index)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}