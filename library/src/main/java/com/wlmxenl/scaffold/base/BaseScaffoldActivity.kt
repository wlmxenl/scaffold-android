package com.wlmxenl.scaffold.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.wlmxenl.scaffold.stateview.IMultiStateView

@Suppress("UNCHECKED_CAST")
abstract class BaseScaffoldActivity<VB : ViewBinding, AppBarView : View> : AppCompatActivity(),
    IBaseScaffold<VB> {
    protected lateinit var binding: VB
    protected var appBarView: AppBarView? = null
    protected var multiStateView: IMultiStateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = onCreateViewBinding(layoutInflater, null, false)
        appBarView = onCreateAppBarView() as? AppBarView
        multiStateView = onCreateMultiStateView()
        val contentView =
            ScaffoldUtils.convertContentView(this, binding.root, appBarView, multiStateView)
        setContentView(contentView)
        onPageViewCreated(savedInstanceState)
        loadData()
    }
}