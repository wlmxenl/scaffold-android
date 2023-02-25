package com.wlmxenl.scaffold.sample.base

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.drake.statelayout.StateLayout
import com.drake.statelayout.Status
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scaffold.stateview.IMultiStateView
import com.wlmxenl.scaffold.stateview.OnStateChangeListener
import com.wlmxenl.scaffold.stateview.ViewState

class CustomMultiStateViewImpl : IMultiStateView {
    private var mStateLayout: StateLayout? = null
    private var mStateChangeListener: OnStateChangeListener? = null
    private var mViewClickConfigList = mutableListOf<Triple<ViewState, Int, OnClickListener?>>()

    override fun convertContentView(contentView: View): View {
        return StateLayout(contentView.context).apply {
            mStateLayout = this
            layoutParams = ViewGroup.LayoutParams(-1, -1)
            addView(contentView, ViewGroup.LayoutParams(-1, -1))
            setContent(contentView)

            setViewForState(ViewState.EMPTY, R.layout.custom_stateview_empty)
            setViewForState(ViewState.LOADING, R.layout.custom_stateview_loading)
            setViewForState(ViewState.ERROR, R.layout.custom_stateview_error)

            onLoading {
                notifyStateChanged(ViewState.LOADING, this, it)
            }
            onEmpty {
                notifyStateChanged(ViewState.EMPTY, this, it)
            }
            onError {
                notifyStateChanged(ViewState.ERROR, this, it)
            }
            onContent {
                notifyStateChanged(ViewState.CONTENT, this, it)
            }
        }
    }

    override fun getMultiStateView(): View? {
        return mStateLayout
    }

    override fun setState(state: ViewState) {
        when (state) {
            ViewState.CONTENT -> mStateLayout?.showContent()
            ViewState.LOADING -> mStateLayout?.showLoading()
            ViewState.EMPTY -> mStateLayout?.showEmpty()
            ViewState.ERROR -> mStateLayout?.showError()
            else -> {
            }
        }
        mViewClickConfigList.forEach {
            if (state == it.first) {
                mStateLayout?.findViewById<View>(it.second)?.setOnClickListener(it.third)
            }
        }
    }

    override fun getState(): ViewState {
        return when (mStateLayout?.status) {
            Status.LOADING -> ViewState.LOADING
            Status.EMPTY -> ViewState.EMPTY
            Status.ERROR -> ViewState.ERROR
            else -> ViewState.CONTENT
        }
    }

    override fun setViewForState(state: ViewState, layoutId: Int) {
        when (state) {
            ViewState.LOADING -> mStateLayout?.loadingLayout = layoutId
            ViewState.EMPTY -> mStateLayout?.emptyLayout = layoutId
            ViewState.ERROR -> mStateLayout?.errorLayout = layoutId
            else -> {
                // not support
            }
        }
    }

    override fun setViewClickListener(
        state: ViewState,
        vararg ids: Int,
        listener: OnClickListener?
    ) {
        ids.forEach { viewId ->
            val iterator = mViewClickConfigList.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.first == state && item.second == viewId) {
                    iterator.remove()
                    break
                }
            }
            mViewClickConfigList.add(Triple(state, viewId, listener))
        }
    }

    override fun setStateChangeListener(listener: OnStateChangeListener) {
        mStateChangeListener = listener
    }

    private fun notifyStateChanged(state: ViewState, stateView: View, tag: Any?) {
        mStateChangeListener?.onStateChanged(state, stateView, tag)
    }
}