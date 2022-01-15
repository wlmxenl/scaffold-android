package cn.dripcloud.scaffold.page

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BasePageFragment<VB : ViewBinding, APPBAR: IAppBarView<out View>> : Fragment(), IBasePage<VB, APPBAR> {
    lateinit var activity: Activity
    protected lateinit var binding: VB
    protected var appBarView: APPBAR? = null
    protected var pageStateView: IPageStateView? = null
    protected var isPageLoaded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = onCreateViewBinding(inflater, container, false)
        pageStateView = onCreatePageStateView()?.apply {
            setRetryClickListener {
                loadPageData()
            }
        }
        appBarView = onCreateAppBarView()
        val pageDelegate = PageDelegate(activity, binding.root, appBarView, pageStateView)
        return pageDelegate.rootView
    }

    override fun onResume() {
        super.onResume()
        // 配合 setMaxLifecycle 实现懒加载
        if (!isPageLoaded && !isHidden) {
            val lazyInit = {
                onLazyInit()
                isPageLoaded = true
            }
            Handler(Looper.getMainLooper()).postDelayed(lazyInit, 160)
        }
    }

    private fun onLazyInit() {
        doBusiness()
        loadPageData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndData(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPageLoaded = false
    }
}