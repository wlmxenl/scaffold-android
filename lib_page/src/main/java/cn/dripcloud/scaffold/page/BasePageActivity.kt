package cn.dripcloud.scaffold.page

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

@Suppress("UNCHECKED_CAST")
abstract class BasePageActivity<VB : ViewBinding, AppBarView : View> : AppCompatActivity(), IBasePage<VB> {
    protected lateinit var activity: Activity
    protected lateinit var binding: VB
    protected var appBarView: AppBarView? = null
    protected var pageStateView: IPageStateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        onBeforeSetContentView()
        binding = onCreateViewBinding(layoutInflater, null, false)
        appBarView = onCreateAppBarView() as? AppBarView
        pageStateView = onCreatePageStateView()?.apply {
            setRetryClickListener {
                loadData()
            }
        }
        val pageDelegate = PageDelegate(activity, binding.root, appBarView, pageStateView)
        setContentView(pageDelegate.rootView)
        onAfterSetContentView()
        onPageViewCreated(savedInstanceState)
        loadData()
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