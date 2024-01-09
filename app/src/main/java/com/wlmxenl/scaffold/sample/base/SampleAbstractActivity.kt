package com.wlmxenl.scaffold.sample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil
import com.wlmxenl.scaffold.base.BaseScaffoldActivity
import com.wlmxenl.scaffold.stateview.IMultiStateView
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 * 业务 BaseActivity 基类
 * @Author wangzhengfu
 * @Date 2022/2/11
 */
abstract class SampleAbstractActivity<VB : ViewBinding, AppBarView : View> : BaseScaffoldActivity<VB, AppBarView>() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): VB {
        return ViewBindingUtil.inflateWithGeneric(this, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置状态栏
        statusBarOnly {
            transparent()
            light = false
        }
    }

    override fun onCreateMultiStateView(): IMultiStateView? {
        return CustomStateLayout()
    }

}