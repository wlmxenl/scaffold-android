package cn.dripcloud.scaffold.page

import android.view.View
import android.view.ViewGroup

interface IPageStateLayout {

    companion object {
        const val STATE_LOADING = 1
        const val STATE_EMPTY = 2
        const val STATE_ERROR = 3
        const val STATE_CONTENT = 4
        const val STATE_CUSTOM = 5
    }

    fun setPageState(state: Int)

    fun getPageState(): Int

    fun getStateContainer(state: Int): ViewGroup?

    fun setRetryClickListener(listener: View.OnClickListener?)

    fun addStateChangeListener (listener: OnStateChangeListener?)

    interface OnStateChangeListener {
        fun onStateChanged(state: Int)
    }
}