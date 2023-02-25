package com.wlmxenl.scaffold.sample.base

import android.content.Context
import android.widget.RelativeLayout
import com.google.android.material.appbar.MaterialToolbar
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.CustomAppbarLayoutBinding
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

/**
 * 应用栏示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
class CustomAppBarView(context: Context) : RelativeLayout(context) {
    private val binding: CustomAppbarLayoutBinding

    init {
        setPadding(0, UltimateBarX.getStatusBarHeight(), 0, 0)
        setBackgroundResource(R.color.appbar_background_color)

        binding = inflate(context, R.layout.custom_appbar_layout, this).run {
            CustomAppbarLayoutBinding.bind(this)
        }
    }

    fun setup(block: MaterialToolbar.() -> Unit) {
        block.invoke(binding.toolbar)
    }

}