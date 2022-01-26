package cn.dripcloud.scaffold.sample.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.dripcloud.scaffold.page.BasePageFragment
import cn.dripcloud.scaffold.page.IPageStateView
import com.blankj.utilcode.util.ReflectUtils
import java.lang.reflect.ParameterizedType

/**
 * BaseFragment 示例
 * @Author wangzhengfu
 * @Date 2022/1/24
 */
abstract class SampleBaseFragment<VB : ViewBinding> : BasePageFragment<VB, SampleAppBarView>() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): VB {
        val vbClazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        return ReflectUtils.reflect(vbClazz).method("inflate", inflater, container, attachToRoot).get()
    }

    override fun onCreateAppBarView(): SampleAppBarView? {
        return SampleAppBarView(requireActivity())
    }

    override fun onCreatePageStateView(): IPageStateView? {
        return SamplePageStateView(requireActivity())
    }
}