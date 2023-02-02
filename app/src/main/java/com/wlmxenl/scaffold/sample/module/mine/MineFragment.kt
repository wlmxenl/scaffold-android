package com.wlmxenl.scaffold.sample.module.mine

import android.os.Bundle
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scafflod.sample.databinding.MineFragmentLayoutBinding

/**
 *
 * @Author wangzhengfu
 * @Date 2022/1/26
 */
class MineFragment : SampleBaseFragment<MineFragmentLayoutBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.let {
            it.hideBackImageView()
            it.setTitle("Mine")
        }
    }
}