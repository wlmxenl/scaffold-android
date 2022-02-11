package cn.dripcloud.scaffold.sample.base

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * BaseActivity 示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
abstract class SampleBaseActivity<VB : ViewBinding> : SampleAbstractActivity<VB, SampleAppBarView>() {

    override fun onCreateAppBarView(): View? {
        return SampleAppBarView(this)
    }

    override fun loadData() {
    }
}