package cn.dripcloud.scaffold.sample.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.dripcloud.scaffold.page.IPageStateView
import cn.dripcloud.scaffold.page.PageState
import cn.dripcloud.scaffold.sample.R
import cn.dripcloud.scaffold.sample.databinding.SamplePagestateViewBinding
import com.blankj.utilcode.util.ClickUtils

/**
 * 页面状态视图示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
class SamplePageStateView(context: Context) : FrameLayout(context), IPageStateView {
    private val binding: SamplePagestateViewBinding
    private var mPageState = PageState.CONTENT

    init {
        binding = inflate(context, R.layout.sample_pagestate_view, this).run {
            SamplePagestateViewBinding.bind(this)
        }
        // 页面基本配置
        setBackgroundResource(R.color.layout_background_color)
        ClickUtils.applyGlobalDebouncing(this) {

        }
        visibility = View.GONE
    }

    override fun getContentView(): View = this

    override fun setPageState(state: PageState) {
    }

    override fun getPageState(): PageState = mPageState

    override fun getView(state: PageState): ViewGroup? {
        return when(state) {
            PageState.LOADING -> binding.stateLoadingLayout
            PageState.EMPTY -> binding.stateEmptyLayout
            PageState.ERROR -> binding.stateErrorLayout
            else -> null
        }
    }

    override fun addStateChangeListener(listener: IPageStateView.OnStateChangeListener?) {
    }

    override fun setRetryClickListener(listener: View.OnClickListener?) {
    }
}