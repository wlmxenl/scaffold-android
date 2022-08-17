package cn.dripcloud.scaffold.sample.module.paging

import android.os.Bundle
import cn.dripcloud.scaffold.arch.databinding.LibArchPagingBinding
import cn.dripcloud.scaffold.arch.paging.PagingExecutor
import cn.dripcloud.scaffold.sample.R
import cn.dripcloud.scaffold.sample.base.SampleBaseFragment
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingFragment : SampleBaseFragment<LibArchPagingBinding>() {
    private lateinit var pagingExecutor: PagingExecutor<Any>

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setTitle("分页测试")

        val adapter = binding.rv.linear().setup {
            addType<SamplePagingItem>(R.layout.paging_recycle_item)
        }

        pagingExecutor = PagingExecutor.Builder<Any>()
            .setAdapter(adapter)
            .bindView(binding.refreshLayout, binding.rv, pageStateView)
            .setPagingRequest(SamplePagingRequest(viewLifecycleOwner))
            .setShowLoadMoreEndView(false)
            .build()
    }

    override fun onEnterAnimationEnd() {
        pagingExecutor.loadFirstPageData()
    }
}