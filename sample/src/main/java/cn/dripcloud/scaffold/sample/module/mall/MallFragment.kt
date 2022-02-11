package cn.dripcloud.scaffold.sample.module.mall

import android.os.Bundle
import cn.dripcloud.scaffold.sample.base.SampleAbstractFragment
import cn.dripcloud.scaffold.sample.databinding.MallFragmentLayoutBinding

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
    }

    override fun onCreateAppBarView() = CustomAppBarView(requireActivity())
}