package cn.dripcloud.scaffold.sample.module.home

import android.os.Bundle
import cn.dripcloud.scaffold.arch.navigation.navigate
import cn.dripcloud.scaffold.sample.R
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
            it.hideBackImageView()
            it.setTitle("Home")
        }

        binding.btnPaging.setOnClickListener {
            navigate(R.id.paging_fragment)
        }
    }

}