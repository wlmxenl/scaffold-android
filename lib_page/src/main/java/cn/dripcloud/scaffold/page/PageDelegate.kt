package cn.dripcloud.scaffold.page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import cn.dripcloud.scaffold.page.databinding.PageContainerBinding

class PageDelegate(
    context: Context,
    layoutView: View,
    appBarView: IAppBarView<out View>? = null,
    pageStateView: IPageStateView? = null) {

    var rootView: View

    init {
        if (appBarView == null && pageStateView == null) {
            rootView = layoutView
        } else {
            // 在原布局外包装一层View，嵌入顶部导航栏和多状态视图
            val binding = PageContainerBinding.inflate(LayoutInflater.from(context))
            rootView = binding.root

            // 顶部导航栏
            var mAppBarViewId = View.NO_ID
            appBarView?.getContentView()?.let {
                if (it.id == View.NO_ID) {
                    it.id = View.generateViewId()
                }
                mAppBarViewId = it.id
                binding.container.addView(it)
                ConstraintSet().apply {
                    clone(binding.container)
                    constrainWidth(it.id, 0)
                    constrainHeight(it.id, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    connect(it.id, ConstraintSet.RIGHT,  ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
                    connect(it.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
                    connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                }.applyTo(binding.container)
            }

            //
            if (layoutView.id == View.NO_ID) {
                layoutView.id = View.generateViewId()
            }
            binding.container.addView(layoutView)
            ConstraintSet().apply {
                clone(binding.container)
                constrainWidth(layoutView.id, 0)
                constrainHeight(layoutView.id, 0)
                connect(layoutView.id, ConstraintSet.END,  ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(layoutView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(layoutView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                if (mAppBarViewId != View.NO_ID) {
                    connect(layoutView.id, ConstraintSet.TOP, mAppBarViewId, ConstraintSet.BOTTOM)
                } else {
                    connect(layoutView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                }
            }.applyTo(binding.container)

            // 多状态视图
            if (pageStateView != null) {
                val multiStateContentView = pageStateView.getContentView()
                if (multiStateContentView.id == View.NO_ID) {
                    multiStateContentView.id = View.generateViewId()
                }
                binding.container.addView(multiStateContentView)
                ConstraintSet().apply {
                    clone(binding.container)
                    constrainWidth(multiStateContentView.id, 0)
                    constrainHeight(multiStateContentView.id, 0)
                    connect(multiStateContentView.id, ConstraintSet.END,  ConstraintSet.PARENT_ID, ConstraintSet.END)
                    connect(multiStateContentView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    connect(multiStateContentView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                    connect(multiStateContentView.id, ConstraintSet.TOP, layoutView.id, ConstraintSet.TOP)
                }.applyTo(binding.container)
            }
        }
    }
}