package cn.dripcloud.scaffold.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BasePageFragment<VB : ViewBinding, APPBAR: IAppBarView<out View>> : Fragment(), IBasePage<VB, APPBAR> {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected var appBarView: APPBAR? = null
    protected var pageStateView: IPageStateView? = null
    private var mTmpSavedFragmentState: Bundle? = null
    private var mIsEnterAnimationEnd = true
    protected var isLazyInitCompleted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = onCreateViewBinding(inflater, container, false)
        pageStateView = onCreatePageStateView()?.apply {
            setRetryClickListener {
                loadData()
            }
        }
        appBarView = onCreateAppBarView()
        val pageDelegate = PageDelegate(requireActivity(), binding.root, appBarView, pageStateView)
        return pageDelegate.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 自定义返回导航
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, mOnBackPressedCallback)
    }

    override fun onResume() {
        super.onResume()
        // 配合 setMaxLifecycle 实现懒加载
        onLazyInit()
    }

    private fun onLazyInit() {
        if (!isLazyInitCompleted && !isHidden) {
            onPageViewCreated(mTmpSavedFragmentState)
            mTmpSavedFragmentState = null
            loadData()
            if (mIsEnterAnimationEnd) {
                onEnterAnimationEnd()
            }
            isLazyInitCompleted = true
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (enter && nextAnim != 0) {
            kotlin.runCatching {
                AnimationUtils.loadAnimation(activity, nextAnim)
            }.onSuccess {
                mIsEnterAnimationEnd = false
                return it.apply {
                    setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            mIsEnterAnimationEnd = true
                            onEnterAnimationEnd()
                        }

                        override fun onAnimationRepeat(animation: Animation?) {}
                    })
                }
            }
        }
        return null
    }

    open fun onEnterAnimationEnd() {}

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mTmpSavedFragmentState = savedInstanceState
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isLazyInitCompleted = false
    }

    private val mOnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!onInterceptBackPressedEvent()) {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    open fun onInterceptBackPressedEvent() = false
}