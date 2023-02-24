package com.wlmxenl.scaffold.paging

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

object PagingUtils {

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