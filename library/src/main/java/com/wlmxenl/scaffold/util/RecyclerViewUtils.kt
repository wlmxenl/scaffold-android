package com.wlmxenl.scaffold.util

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wlmxenl.scaffold.pagination.loadState.FullSpanAdapterType

object RecyclerViewUtils {

    fun setStaggeredGridFullSpan(viewHolder: RecyclerView.ViewHolder) {
        val layoutParams = viewHolder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    fun setGridFullSpan(concatAdapter: ConcatAdapter, gridLayoutManager: GridLayoutManager) {
        gridLayoutManager.run {
            val oldSpanSizeLookup = spanSizeLookup
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < 0) return 1
                    return when(getAdapterByItemPosition(concatAdapter, position)) {
                        is FullSpanAdapterType -> spanCount
                        else -> oldSpanSizeLookup?.getSpanSize(position) ?: 1
                    }
                }
            }
        }
    }

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