package cn.dripcloud.scaffold.sample.module.paging_test

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.dripcloud.scaffold.arch.paging.BaseBinderLoadMoreAdapter
import cn.dripcloud.scaffold.arch.paging.PagingExecutor
import cn.dripcloud.scaffold.sample.R
import cn.dripcloud.scaffold.sample.base.SampleBaseFragment
import cn.dripcloud.scaffold.sample.databinding.PagingFragmentBinding
import com.blankj.utilcode.util.SizeUtils
import me.jingbin.library.decoration.SpacesItemDecoration

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingFragment : SampleBaseFragment<PagingFragmentBinding>() {
    private lateinit var pagingExecutor: PagingExecutor<Any>
    private val adapter by lazy {
        BaseBinderLoadMoreAdapter().apply {
            addItemBinder(SamplePagingItemBinder())
        }
    }

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setTitle("分页测试")

        binding.rvList.run {
            layoutManager = LinearLayoutManager(requireActivity())
            addItemDecoration(SpacesItemDecoration(requireActivity()).apply {
                setParam(R.color.rv_divider_color, SizeUtils.dp2px(0.6f), 15f, 15f)
            })
        }

        pagingExecutor = PagingExecutor.Builder<Any>()
            .setAdapter(adapter)
            .bindView(binding.refreshLayout, binding.rvList, pageStateView)
            .setPagingRequest(SamplePagingRequest(viewLifecycleOwner.lifecycleScope))
            .build()
    }

    override fun onEnterAnimationEnd() {
        pagingExecutor.loadData()
    }
}