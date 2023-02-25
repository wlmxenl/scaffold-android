package com.wlmxenl.scaffold.stateview

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 *
 * Created by wangzf on 2023/2/24
 */
interface IMultiStateView {

    /**
     * @param contentView 内容布局
     * @return 包装完多状态视图后的布局
     */
    fun convertContentView(contentView: View): View

    fun getMultiStateView(): View?

    fun setState(state: ViewState)

    fun getState(): ViewState

    fun setViewForState(state: ViewState, @LayoutRes layoutId: Int)

    fun setViewClickListener(state: ViewState, @IdRes vararg ids: Int, listener: View.OnClickListener?)

    fun setStateChangeListener(listener: OnStateChangeListener)
}

interface OnStateChangeListener {
    fun onStateChanged(state: ViewState, stateView: View, tag: Any?)
}

inline fun <reified V> IMultiStateView.get() : V? {
    return (this as? IMultiStateView)?.getMultiStateView() as? V
}