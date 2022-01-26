package cn.dripcloud.scaffold.sample.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import cn.dripcloud.scaffold.page.BasePageActivity
import cn.dripcloud.scaffold.page.IPageStateView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ReflectUtils
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import java.lang.reflect.ParameterizedType

/**
 * BaseActivity 示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
abstract class SampleBaseActivity<VB : ViewBinding> : BasePageActivity<VB, SampleAppBarView>() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): VB {
        // 为了每个子类少写一行代码，此处使用反射初始化 VB
        // 如介意反射的影响，可在此类添加一个包含初始化 VB 的匿名函数作为构造参数的构造方法
        val vbClazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        LogUtils.d(vbClazz)
        // return vbClazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, inflater) as VB
        return ReflectUtils.reflect(vbClazz).method("inflate", inflater).get()
    }

    override fun onCreateAppBarView(): SampleAppBarView? {
        return SampleAppBarView(this)
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