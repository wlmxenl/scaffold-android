package com.wlmxenl.scaffold.pagination

import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemAttached

open class ScaffoldBrvAdapter : BindingAdapter() {
    private var onViewAttachStateChangeListeners: MutableList<ItemAttached>? = null

    override fun onViewAttachedToWindow(holder: BindingViewHolder) {
        super.onViewAttachedToWindow(holder)
        onViewAttachStateChangeListeners?.forEach {
            it.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: BindingViewHolder) {
        super.onViewDetachedFromWindow(holder)
        onViewAttachStateChangeListeners?.forEach {
            it.onViewDetachedFromWindow(holder)
        }
    }

    fun addOnViewAttachStateChangeListener(listener: ItemAttached) {
        if (onViewAttachStateChangeListeners == null) {
            onViewAttachStateChangeListeners = ArrayList()
        }
        if (onViewAttachStateChangeListeners?.contains(listener) == true) return
        onViewAttachStateChangeListeners?.add(listener)
    }

    fun removeOnViewAttachStateChangeListener(listener: ItemAttached) {
        onViewAttachStateChangeListeners?.remove(listener)
    }

    fun clearOnViewAttachStateChangeListener() {
        onViewAttachStateChangeListeners?.clear()
    }
}