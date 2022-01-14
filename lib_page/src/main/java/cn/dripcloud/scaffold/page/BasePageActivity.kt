package cn.dripcloud.scaffold.page

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BasePageActivity<VB : ViewBinding, APPBAR: IAppBarView<out View>> :
    AppCompatActivity(), IBasePage<VB, APPBAR> {
    protected lateinit var activity: Activity
    protected lateinit var binding: VB
    protected var appBarView: APPBAR? = null
    protected var pageStateView: IPageStateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        onBeforeSetContentView()
        binding = onCreateViewBinding(layoutInflater, null, false)
        appBarView = onCreateAppBarView()
        pageStateView = onCreateMultiStateView()?.apply {
            setRetryClickListener {
                loadPageData()
            }
        }
        val pageDelegate = PageDelegate(activity, binding.root, appBarView, pageStateView)
        setContentView(pageDelegate.rootView)
        onAfterSetContentView()
        initViewAndData(binding.root, savedInstanceState)
        doBusiness()
        loadPageData()
    }

    /**
     * 在 setContentView() 之前执行
     */
    open fun onBeforeSetContentView() {}

    /**
     * 在 setContentView() 之后执行
     */
    open fun onAfterSetContentView() {}
}