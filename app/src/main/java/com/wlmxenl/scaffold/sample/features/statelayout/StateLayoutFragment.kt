package com.wlmxenl.scaffold.sample.features.statelayout

import android.os.Bundle
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.SampleStateLayoutBinding
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scaffold.stateview.ViewState

class StateLayoutFragment : SampleBaseFragment<SampleStateLayoutBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setup {
            setTitle(R.string.sample_state_layout)
            inflateMenu(R.menu.state_options)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.state_content -> multiStateView?.setState(ViewState.CONTENT)
                    R.id.state_loading -> multiStateView?.setState(ViewState.LOADING)
                    R.id.state_empty -> multiStateView?.setState(ViewState.EMPTY)
                    R.id.state_error -> multiStateView?.setState(ViewState.ERROR)
                }
                true
            }
        }

        // 移除重试按钮事件
        // multiStateView?.setViewClickListener(ViewState.ERROR, R.id.btn_retry, listener = null)
    }
}