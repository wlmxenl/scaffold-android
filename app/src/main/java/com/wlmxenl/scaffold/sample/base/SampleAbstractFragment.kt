package com.wlmxenl.scaffold.sample.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scaffold.stateview.IMultiStateView
import com.wlmxenl.scaffold.stateview.ViewState

/**
 * 业务 BaseFragment 基类
 * @Author wangzhengfu
 * @Date 2022/2/11
 */
abstract class SampleAbstractFragment<VB : ViewBinding, AppBarView : View> : com.wlmxenl.scaffold.base.BaseScafflodFragment<VB, AppBarView>() {
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): VB {
        return ViewBindingUtil.inflateWithGeneric(this, inflater, container, attachToRoot)
    }

    override fun onCreateMultiStateView(): IMultiStateView? {
        return CustomStateLayout().apply {
            setViewClickListener(ViewState.ERROR, R.id.btn_retry) {
                loadData()
            }
        }
    }
}