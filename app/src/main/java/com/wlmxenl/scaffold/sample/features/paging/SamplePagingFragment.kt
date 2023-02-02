package com.wlmxenl.scaffold.sample.features.paging

import android.os.Bundle
import com.wlmxenl.scaffold.paging.PagingBindingAdapter
import com.wlmxenl.scaffold.paging.PagingExecutor
import com.drake.brv.utils.linear
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scafflod.sample.databinding.PagingListLayoutBinding

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingFragment : SampleBaseFragment<PagingListLayoutBinding>() {
    private lateinit var pagingExecutor: PagingExecutor<Any>

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setTitle(R.string.sample_paging)

        binding.rv.linear()
        val contentAdapter = PagingBindingAdapter().apply {
            addType<SamplePagingItem>(R.layout.paging_recycle_item)
        }

        pagingExecutor = PagingExecutor.Builder<Any>()
            .setAdapter(contentAdapter)
            .bindView(binding.refreshLayout, binding.rv, stateLayout)
            .setPagingRequest(SamplePagingRequest(viewLifecycleOwner))
            .build()
    }

    override fun onEnterAnimationEnd() {
        pagingExecutor.loadFirstPageData()
    }
}