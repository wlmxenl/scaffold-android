package cn.dripcloud.scaffold.sample.module.mall

import android.os.Bundle
import android.view.View
import cn.dripcloud.scaffold.sample.base.SampleBaseFragment
import cn.dripcloud.scaffold.sample.databinding.MallFragmentLayoutBinding

/**
 *
 * @Author wangzhengfu
 * @Date 2022/1/26
 */
class MallFragment : SampleBaseFragment<MallFragmentLayoutBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.let {
            it.binding.ivBack.visibility = View.GONE
        }

    }

    override fun loadData() {
    }

}