package com.wlmxenl.scaffold.sample.base

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * BaseFragment 示例
 * @Author wangzhengfu
 * @Date 2022/1/24
 */
abstract class SampleBaseFragment<VB : ViewBinding> : SampleAbstractFragment<VB, SampleAppBarView>() {

    override fun onCreateAppBarView(): View? {
        return SampleAppBarView(requireActivity()).apply {
            setBackClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun loadData() {
    }
}