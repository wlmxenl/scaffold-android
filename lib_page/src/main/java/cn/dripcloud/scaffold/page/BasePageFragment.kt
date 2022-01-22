package cn.dripcloud.scaffold.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BasePageFragment<VB : ViewBinding, APPBAR: IAppBarView<out View>> : Fragment(), IBasePage<VB, APPBAR> {
    protected lateinit var binding: VB
    protected var appBarView: APPBAR? = null
    protected var pageStateView: IPageStateView? = null
    protected var isPageLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = onCreateViewBinding(inflater, container, false)
        pageStateView = onCreatePageStateView()?.apply {
            setRetryClickListener {
                loadData()
            }
        }
        appBarView = onCreateAppBarView()
        val pageDelegate = PageDelegate(requireActivity(), binding.root, appBarView, pageStateView)
        return pageDelegate.rootView
    }

    override fun onResume() {
        super.onResume()
        // 配合 setMaxLifecycle 实现懒加载
        if (!isPageLoaded && !isHidden) {
            val lazyInit = {
                onLazyInitInternal()
                isPageLoaded = true
            }
            Handler(Looper.getMainLooper()).postDelayed(lazyInit, 160)
        }
    }

    private fun onLazyInitInternal() {
        initView()
        loadData()
        // 自定义返回导航
        requireActivity().onBackPressedDispatcher.addCallback(this, mOnBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPageLoaded = false
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