package cn.dripcloud.scaffold.page

import android.view.View
import android.view.ViewGroup

interface IPageStateView {

    fun getContentView(): View

    fun setPageState(state: PageState)

    fun getPageState(): PageState

    fun getView(state: PageState): ViewGroup?

    fun addStateChangeListener (listener: OnStateChangeListener?)

    fun setRetryClickListener(listener: View.OnClickListener?)

    interface OnStateChangeListener {
        fun onStateChanged(state: PageState)
    }
}