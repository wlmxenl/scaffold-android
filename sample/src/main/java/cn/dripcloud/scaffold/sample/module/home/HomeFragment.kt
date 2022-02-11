package cn.dripcloud.scaffold.sample.module.home

import android.os.Bundle
import android.view.View
import cn.dripcloud.scaffold.sample.base.SampleBaseFragment
import cn.dripcloud.scaffold.sample.databinding.HomeFragmentLayoutBinding

/**
 *
 * @Author wangzhengfu
 * @Date 2022/1/26
 */
class HomeFragment : SampleBaseFragment<HomeFragmentLayoutBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.let {
            it.binding.ivBack.visibility = View.GONE
            it.setTitle("Home")
        }
    }

}