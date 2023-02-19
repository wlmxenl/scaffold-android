package com.wlmxenl.scaffold.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wlmxenl.scaffold.statelayout.StateLayoutProvider

interface IBaseScaffold<VB : ViewBinding> {
    fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): VB

    fun onCreateAppBarView(): View?

    fun getStateLayoutProvider(): StateLayoutProvider?

    fun onPageViewCreated(savedInstanceState: Bundle?)

    fun loadData()
}