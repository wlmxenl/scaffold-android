@file:JvmName("ViewPagerExtKt")
package com.wlmxenl.scaffold.ext

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * 设置 ViewPager 首次绑定 adapter 后显示的页面下标。
 *
 * 该方法仅在当前未绑定 adapter 时注册一次默认下标。
 * 后续绑定到有效 adapter 后，会通过 [ViewPager.setCurrentItem] 设置初始页面并移除监听。
 * 如果调用时已经存在 adapter，则不会执行任何操作。
 */
fun ViewPager.setDefaultItemIndex(index: Int) {
    if (adapter != null) {
        return
    }

    val defaultIndex = index.coerceAtLeast(0)
    val listener = object : ViewPager.OnAdapterChangeListener {
        override fun onAdapterChanged(
            viewPager: ViewPager,
            oldAdapter: PagerAdapter?,
            newAdapter: PagerAdapter?
        ) {
            if (newAdapter == null || newAdapter.count <= 0) {
                return
            }
            viewPager.setCurrentItem(defaultIndex, false)
            viewPager.removeOnAdapterChangeListener(this)
        }
    }
    addOnAdapterChangeListener(listener)
}
