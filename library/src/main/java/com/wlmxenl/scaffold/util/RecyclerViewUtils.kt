package com.wlmxenl.scaffold.util

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wlmxenl.scaffold.paging.PagingUtils
import com.wlmxenl.scaffold.paging.loadState.FullSpanAdapterType

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
                    return when(PagingUtils.getAdapterByItemPosition(concatAdapter, position)) {
                        is FullSpanAdapterType -> spanCount
                        else -> oldSpanSizeLookup?.getSpanSize(position) ?: 1
                    }
                }
            }
        }
    }


}