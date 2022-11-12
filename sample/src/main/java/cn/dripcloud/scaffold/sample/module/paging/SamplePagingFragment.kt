package cn.dripcloud.scaffold.sample.module.paging

import android.os.Bundle
import cn.dripcloud.scaffold.arch.paging.PagingBindingAdapter
import cn.dripcloud.scaffold.arch.paging.PagingExecutor
import cn.dripcloud.scaffold.sample.R
import cn.dripcloud.scaffold.sample.base.SampleBaseFragment
import cn.dripcloud.scaffold.sample.databinding.PagingListLayoutBinding
import com.drake.brv.utils.linear

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingFragment : SampleBaseFragment<PagingListLayoutBinding>() {
    private lateinit var pagingExecutor: PagingExecutor<Any>

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setTitle("分页测试")

        binding.rv.linear()
        val contentAdapter = PagingBindingAdapter().apply {
            addType<SamplePagingItem>(R.layout.paging_recycle_item)
        }

        pagingExecutor = PagingExecutor.Builder<Any>()
            .setAdapter(contentAdapter)
            .bindView(binding.refreshLayout, binding.rv, pageStateView)
            .setPagingRequest(SamplePagingRequest(viewLifecycleOwner))
            .build()
    }

    override fun onEnterAnimationEnd() {
        pagingExecutor.loadFirstPageData()
    }
}