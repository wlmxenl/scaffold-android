package cn.dripcloud.scaffold.arch.viewpager

import androidx.viewpager.widget.ViewPager

/**
 * 设置 ViewPager 默认下标
 * @param index Int
 */
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