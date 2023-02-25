package com.wlmxenl.scaffold.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wlmxenl.scaffold.stateview.IMultiStateView

interface IBaseScaffold<VB : ViewBinding> {

    fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): VB

    fun onCreateAppBarView(): View?

    fun onCreateMultiStateView(): IMultiStateView?

    fun onPageViewCreated(savedInstanceState: Bundle?)

    fun loadData()
}