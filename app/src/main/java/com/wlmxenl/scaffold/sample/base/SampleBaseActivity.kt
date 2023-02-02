package com.wlmxenl.scaffold.sample.base

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * BaseActivity 示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
abstract class SampleBaseActivity<VB : ViewBinding> : SampleAbstractActivity<VB, SampleAppBarView>() {

    override fun onCreateAppBarView(): View? {
        return SampleAppBarView(this).apply {
            setBackClickListener {
                onBackPressed()
            }
        }
    }

    override fun loadData() {
    }
}