package com.wlmxenl.scaffold.sample.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wlmxenl.scaffold.base.BaseScaffoldActivity
import com.dylanc.viewbinding.base.ViewBindingUtil
import com.wlmxenl.scaffold.statelayout.StateLayoutProvider
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

    override fun getStateLayoutProvider(): StateLayoutProvider? {
        return CustomStateLayoutProviderImpl()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onBeforeSetContentView() {
        // 设置强制竖屏
        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    override fun onAfterSetContentView() {
        // 设置状态栏
        statusBarOnly {
            transparent()
            light = false
        }
    }
}