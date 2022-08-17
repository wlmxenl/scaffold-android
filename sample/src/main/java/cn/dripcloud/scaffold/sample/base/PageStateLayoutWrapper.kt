package cn.dripcloud.scaffold.sample.base

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import cn.dripcloud.scaffold.page.IContentLayoutWrapper
import cn.dripcloud.scaffold.page.IPageStateLayout
import cn.dripcloud.scaffold.sample.R
import com.drake.statelayout.StateLayout

/**
 * 包裹跟布局多状态布局实现类
 * @author wangzf
 * @date 2022/8/17
 */
class PageStateLayoutWrapper : IContentLayoutWrapper {
    private var mStateLayout: StateLayout? = null
    private val mStateChangeListeners = mutableListOf<IPageStateLayout.OnStateChangeListener>()
    private var mCurrentPageState = IPageStateLayout.STATE_CONTENT

    override fun wrap(pageRootLayout: ConstraintLayout, pageContentLayout: View, appBarViewId: Int) {
        mStateLayout = StateLayout(pageRootLayout.context).apply {
            id = R.id.page_state_layout
            addView(pageContentLayout, FrameLayout.LayoutParams(-1, -1))
            setContent(pageContentLayout)
        }
        val stateLayoutId = mStateLayout!!.id

        if (pageContentLayout.id == View.NO_ID) {
            pageContentLayout.id = R.id.page_content_layout
        }
        pageRootLayout.addView(mStateLayout)
        ConstraintSet().apply {
            clone(pageRootLayout)
            constrainWidth(stateLayoutId, 0)
            constrainHeight(stateLayoutId, 0)
            connect(stateLayoutId, ConstraintSet.END,  ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(stateLayoutId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(stateLayoutId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            if (appBarViewId != View.NO_ID) {
                connect(stateLayoutId, ConstraintSet.TOP, appBarViewId, ConstraintSet.BOTTOM)
            } else {
                connect(stateLayoutId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }
        }.applyTo(pageRootLayout)
    }

    override fun setPageState(state: Int) {
        when (state) {
            IPageStateLayout.STATE_LOADING -> mStateLayout?.showLoading()
            IPageStateLayout.STATE_CONTENT -> mStateLayout?.showContent()
            IPageStateLayout.STATE_EMPTY -> mStateLayout?.showEmpty()
            IPageStateLayout.STATE_ERROR -> mStateLayout?.showError()
            else -> {
                // 暂不支持
            }
        }
        mCurrentPageState = state
        mStateChangeListeners.forEach {
            it.onStateChanged(state)
        }
    }

    override fun getPageState(): Int {
        return mCurrentPageState
    }

    override fun getStateContainer(state: Int): ViewGroup? {
        return null
    }

    override fun setRetryClickListener(listener: View.OnClickListener?) {
    }

    override fun addStateChangeListener(listener: IPageStateLayout.OnStateChangeListener?) {
        listener?.let {
            if (!mStateChangeListeners.contains(it)) {
                mStateChangeListeners.add(it)
            }
        }
    }
}