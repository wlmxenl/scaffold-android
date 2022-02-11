package cn.dripcloud.scaffold.sample.module.mine

import android.os.Bundle
import android.view.View
import cn.dripcloud.scaffold.sample.base.SampleBaseFragment
import cn.dripcloud.scaffold.sample.databinding.MineFragmentLayoutBinding

/**
 *
 * @Author wangzhengfu
 * @Date 2022/1/26
 */
class MineFragment : SampleBaseFragment<MineFragmentLayoutBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.let {
            it.binding.ivBack.visibility = View.GONE
            it.setTitle("Mine")
        }
    }
}