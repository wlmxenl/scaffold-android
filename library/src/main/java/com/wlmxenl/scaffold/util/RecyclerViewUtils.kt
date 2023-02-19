package com.wlmxenl.scaffold.util

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * Created by wangzf on 2023/2/19
 */
object RecyclerViewUtils {

    @JvmStatic
    fun getAdapterByItemPosition(concatAdapter: ConcatAdapter, position: Int):
            RecyclerView.Adapter<out RecyclerView.ViewHolder>? {
        var pos = position
        val adapters = concatAdapter.adapters
        for (adapter in adapters) {
            when {
                pos >= adapter.itemCount -> {
                    pos -= adapter.itemCount
                }
                pos < 0 -> return null
                else -> return adapter
            }
        }
        return null
    }
}