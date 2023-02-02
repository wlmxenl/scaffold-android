package com.wlmxenl.scaffold.sample.module.home

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wlmxenl.scaffold.sample.common.dp2pxInt
import com.wlmxenl.scaffold.sample.features.SampleActivity
import com.drake.brv.utils.setup
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scafflod.sample.databinding.HomeFragmentLayoutBinding
import com.wlmxenl.scafflod.sample.databinding.HomeRecycleItemBinding
import com.wlmxenl.scaffold.statelayout.StateLayoutDelegate
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager

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

        binding.rv.run {
            layoutManager = FlexboxLayoutManager(requireActivity())
            addItemDecoration(FlexboxItemDecoration(requireActivity()).apply {
                setDrawable(object : ColorDrawable() {
                    override fun getIntrinsicWidth(): Int {
                        return 8f.dp2pxInt()
                    }

                    override fun getIntrinsicHeight(): Int {
                        return 8f.dp2pxInt()
                    }
                })
            })
        }

        binding.rv.setup {
            addType<Pair<Int, Int>>(R.layout.home_recycle_item)

            onBind {
                getBinding<HomeRecycleItemBinding>().context = requireActivity() as AppCompatActivity
            }

            onClick(R.id.tv_demo_item) {
                startActivity(Intent(requireActivity(), SampleActivity::class.java).apply {
                    putExtra("startDestination", getModel<Pair<Int, Int>>().second)
                })
            }
        }.models = mutableListOf<Any?>().apply {
            add(Pair(R.string.sample_paging, R.id.paging_fragment))
            add(Pair(R.string.sample_state_layout, R.id.state_layout_fragment))
        }
    }

    override fun getStateLayoutDelegate(): StateLayoutDelegate? = null

}