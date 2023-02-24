package com.wlmxenl.scaffold.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.wlmxenl.scaffold.statelayout.IStateLayout
import com.wlmxenl.scaffold.statelayout.StateLayoutProvider

@Suppress("UNCHECKED_CAST")
abstract class BaseScaffoldActivity<VB : ViewBinding, AppBarView : View> : AppCompatActivity(),
    IBaseScaffold<VB> {
    protected lateinit var binding: VB
    protected var appBarView: AppBarView? = null
    protected var stateLayout: IStateLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onCreateViewBinding(layoutInflater, null, false)
        appBarView = onCreateAppBarView() as? AppBarView
        stateLayout = getStateLayoutProvider()
        val contentView = ScaffoldUtils.convertContentView(
            this,
            binding.root, appBarView, stateLayout as? StateLayoutProvider
        )
        setContentView(contentView)
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