package cn.dripcloud.scaffold.sample.module.mall

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.dripcloud.scaffold.page.IPageStateLayout
import cn.dripcloud.scaffold.sample.base.SampleAbstractFragment
import cn.dripcloud.scaffold.sample.databinding.MallFragmentLayoutBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @Author wangzhengfu
 * @Date 2022/1/26
 */
class MallFragment : SampleAbstractFragment<MallFragmentLayoutBinding, CustomAppBarView>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.let {
            // ...
        }
    }

    override fun loadData() {
        pageStateView?.setPageState(IPageStateLayout.STATE_LOADING)
        lifecycleScope.launch {
            delay(1000)
            pageStateView?.setPageState(IPageStateLayout.STATE_CONTENT)
        }
    }

    override fun onCreateAppBarView() = CustomAppBarView(requireActivity())
}