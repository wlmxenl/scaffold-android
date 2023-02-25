package com.wlmxenl.scaffold.sample

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.drake.brv.utils.setup
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.ActivityMainBinding
import com.wlmxenl.scafflod.sample.databinding.FeatureRecycleItemBinding
import com.wlmxenl.scaffold.sample.base.SampleBaseActivity
import com.wlmxenl.scaffold.sample.common.dp2pxInt
import com.wlmxenl.scaffold.sample.features.SampleActivity
import com.wlmxenl.scaffold.stateview.IMultiStateView

class MainActivity : SampleBaseActivity<ActivityMainBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setup {
            setTitle(R.string.app_name)
        }

        binding.rv.run {
            layoutManager = FlexboxLayoutManager(this@MainActivity)
            addItemDecoration(FlexboxItemDecoration(this@MainActivity).apply {
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
            addType<Pair<Int, Int>>(R.layout.feature_recycle_item)

            onBind {
                getBinding<FeatureRecycleItemBinding>().context = this@MainActivity
            }

            onClick(R.id.tv_demo_item) {
                startActivity(Intent(this@MainActivity, SampleActivity::class.java).apply {
                    putExtra("startDestination", getModel<Pair<Int, Int>>().second)
                })
            }
        }.models = mutableListOf<Any?>().apply {
            add(Pair(R.string.sample_state_layout, R.id.state_layout_fragment))
            add(Pair(R.string.sample_paging, R.id.paging_fragment))
        }
    }

    override fun onCreateMultiStateView(): IMultiStateView? = null

}