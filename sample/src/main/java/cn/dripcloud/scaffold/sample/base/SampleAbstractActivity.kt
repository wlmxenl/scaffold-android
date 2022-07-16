package cn.dripcloud.scaffold.sample.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.dripcloud.scaffold.page.BasePageActivity
import cn.dripcloud.scaffold.page.IPageStateView
import com.dylanc.viewbinding.base.ViewBindingUtil
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 * 业务 BaseActivity 基类
 * @Author wangzhengfu
 * @Date 2022/2/11
 */
abstract class SampleAbstractActivity<VB : ViewBinding, AppBarView : View> : BasePageActivity<VB, AppBarView>() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): VB {
        // 为了每个子类少写一行代码，此处使用反射初始化 VB
        // 如介意反射的影响，可在此类添加一个包含初始化 VB 的匿名函数作为构造参数的构造方法
        return ViewBindingUtil.inflateWithGeneric(this, inflater)
    }

    override fun onCreatePageStateView(): IPageStateView? {
        return SamplePageStateView(this)
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