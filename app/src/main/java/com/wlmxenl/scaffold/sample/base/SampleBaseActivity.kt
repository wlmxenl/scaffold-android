package com.wlmxenl.scaffold.sample.base

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * BaseActivity 示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
abstract class SampleBaseActivity<VB : ViewBinding> : SampleAbstractActivity<VB, CustomAppBarView>() {

    override fun onCreateAppBarView(): View? {
        return CustomAppBarView(this).apply {
            setup {

            }
        }
    }

    override fun loadData() {
    }
}