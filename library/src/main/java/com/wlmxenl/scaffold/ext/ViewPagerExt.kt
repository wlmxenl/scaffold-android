@file:JvmName("ViewPagerExtKt")
package com.wlmxenl.scaffold.ext

import androidx.viewpager.widget.ViewPager

fun ViewPager.setDefaultItemIndex(index: Int) {
    try {
        val clazz = Class.forName("androidx.viewpager.widget.ViewPager")
        val field = clazz.getDeclaredField("mCurItem")
        field.isAccessible = true
        field.setInt(this, index)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}