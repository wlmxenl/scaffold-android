package com.wlmxenl.scaffold.sample.features.paging

import android.os.Bundle
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.staggered
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.PagingListLayoutBinding
import com.wlmxenl.scaffold.navigation.scaffoldNavigate
import com.wlmxenl.scaffold.paging.PagingExecutor
import com.wlmxenl.scaffold.paging.ScaffoldPagingAdapter
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingFragment : SampleBaseFragment<PagingListLayoutBinding>() {
    private lateinit var pagingExecutor: PagingExecutor<Any>
    private val type by lazy {
        arguments?.getInt("type") ?: R.id.linear
    }

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setup {
            title = "${getString(R.string.sample_paging)} ${getTypeStr()}"
            inflateMenu(R.menu.layoutmanager_options)
            setOnMenuItemClickListener {
                onLayoutManagerChanged(it.itemId)
                true
            }
        }

        when (type) {
            R.id.linear ->  {
                binding.rv
                    .divider {
                        setDivider(1, true)
                    }
                    .linear()
            }
            R.id.grid -> {
                binding.rv
                    .divider {
                        orientation = DividerOrientation.GRID
                        includeVisible = true
                        setDivider(10, true)
                    }
                    .grid(2)
            }
            R.id.staggered_grid -> {
                binding.rv
                    .divider {
                        orientation = DividerOrientation.GRID
                        includeVisible = true
                        setDivider(10, true)
                    }
                    .staggered(2)
            }
        }

        val contentAdapter = ScaffoldPagingAdapter().apply {
            when (type) {
                R.id.linear -> addType<SamplePagingItem>(R.layout.paging_linear_recycle_item)
                R.id.grid -> addType<SamplePagingItem>(R.layout.paging_grid_recycle_item)
                R.id.staggered_grid -> addType<SamplePagingItem>(R.layout.paging_staggered_grid_recycle_item)
            }
        }

        pagingExecutor = PagingExecutor.Builder<Any>()
            .setAdapter(contentAdapter)
            .bindView(binding.refreshLayout, binding.rv, multiStateView)
            .setPagingRequest(SamplePagingRequest(viewLifecycleOwner))
            .build()
    }

    override fun onEnterAnimationEnd() {
        pagingExecutor.loadFirstPageData()
    }

    private fun onLayoutManagerChanged(id: Int) {
        if (type == id) return
        scaffoldNavigate(R.id.paging_fragment, Bundle().apply {
            putInt("type", id)
        })
    }

    private fun getTypeStr() = when (type) {
        R.id.linear -> "linear"
        R.id.grid -> "grid"
        else -> "staggered_grid"
    }
}