package com.wlmxenl.scaffold.statelayout

import android.view.View
import androidx.annotation.IntDef

interface IStateLayout {

    @IntDef(LOADING, EMPTY, ERROR, CONTENT, CUSTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class State

    companion object {
        const val LOADING = 1
        const val EMPTY = 2
        const val ERROR = 3
        const val CONTENT = 4
        const val CUSTOM = 5
    }

    fun setState(@State state: Int)

    fun getState(): Int

    fun setStateChangeListener(listener: OnStateChangeListener)

    interface OnStateChangeListener {
        fun onStateChanged(state: Int, stateView: View, tag: Any?)
    }
}

inline fun <reified V> IStateLayout.get() : V? {
    return (this as? StateLayoutProvider)?.getStateLayout() as? V
}