package com.wlmxenl.scaffold.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.wlmxenl.scaffold.R
import com.wlmxenl.scaffold.stateview.IMultiStateView

/**
 * 页面跟布局相关操作工具类
 * @author wangzf
 * @date 2022/8/20
 */
internal object ScaffoldUtils {

    fun convertContentView(
        context: Context,
        contentView: View,
        appBarView: View? = null,
        multiStateView: IMultiStateView?
    ): View {
        // 无导航栏、无状态布局
        if (appBarView == null && multiStateView == null) {
            return contentView
        }

        // 无导航栏、有状态布局
        if (appBarView == null && multiStateView != null) {
            return multiStateView.convertContentView(contentView)
        }

        val wrapperLayout = ConstraintLayout(context).apply {
            id = R.id.scaffold_container
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }

        // 添加顶部导航栏
        var mAppBarViewId = View.NO_ID
        appBarView?.let {
            if (it.id == View.NO_ID) {
                it.id = R.id.scaffold_appbar
            }
            mAppBarViewId = it.id
            wrapperLayout.addView(it, ConstraintLayout.LayoutParams(-1, -2).apply {
                topToTop = ConstraintSet.PARENT_ID
            })
        }

        wrapperLayout.addView(multiStateView?.convertContentView(contentView) ?: contentView,
            ConstraintLayout.LayoutParams(-1, 0).apply {
            if (mAppBarViewId != View.NO_ID) {
                topToBottom = mAppBarViewId
            } else {
                topToTop = ConstraintSet.PARENT_ID
            }
            bottomToBottom = ConstraintSet.PARENT_ID
        })

        return wrapperLayout
    }
}