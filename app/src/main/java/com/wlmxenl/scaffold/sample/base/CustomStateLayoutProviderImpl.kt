package com.wlmxenl.scaffold.sample.base

import android.view.View
import android.view.ViewGroup
import com.drake.statelayout.StateLayout
import com.drake.statelayout.Status
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scaffold.statelayout.IStateLayout
import com.wlmxenl.scaffold.statelayout.StateLayoutProvider

class CustomStateLayoutProviderImpl : StateLayoutProvider() {
    private var mStateLayout: StateLayout? = null
    private var mStateChangeListener: IStateLayout.OnStateChangeListener? = null

    override fun convertContentView(contentView: View): View {
        return StateLayout(contentView.context).apply {
            mStateLayout = this
            layoutParams = ViewGroup.LayoutParams(-1, -1)
            addView(contentView, ViewGroup.LayoutParams(-1, -1))
            setContent(contentView)

            emptyLayout = R.layout.page_state_empty
            loadingLayout = R.layout.page_state_loading
            errorLayout = R.layout.page_state_error

            onLoading {
                notifyStateChanged(IStateLayout.LOADING, this, it)
            }
            onEmpty {
                notifyStateChanged(IStateLayout.EMPTY, this, it)
            }
            onError {
                notifyStateChanged(IStateLayout.ERROR, this, it)
            }
            onContent {
                notifyStateChanged(IStateLayout.CONTENT, this, it)
            }
        }
    }

    override fun setState(state: Int) {
        when (state) {
            IStateLayout.CONTENT -> mStateLayout?.showContent()
            IStateLayout.LOADING -> mStateLayout?.showLoading()
            IStateLayout.EMPTY -> mStateLayout?.showEmpty()
            IStateLayout.ERROR -> mStateLayout?.showError()
            else -> {
            }
        }
    }

    override fun getState(): Int {
        return when (mStateLayout?.status) {
            Status.LOADING -> IStateLayout.LOADING
            Status.EMPTY -> IStateLayout.EMPTY
            Status.ERROR -> IStateLayout.ERROR
            else -> IStateLayout.CONTENT
        }
    }

    override fun setStateChangeListener(listener: IStateLayout.OnStateChangeListener) {
        mStateChangeListener = listener
    }

    override fun getStateLayout(): View? {
        return mStateLayout
    }

    private fun notifyStateChanged(state: Int, stateView: View, tag: Any?) {
        mStateChangeListener?.onStateChanged(state, stateView, tag)
    }
}