package cn.dripcloud.scaffold.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface IBasePage<VB : ViewBinding> {

    fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): VB

    fun onCreateAppBarView(): View?

    fun onCreatePageStateView(): IPageStateLayout?

    fun onPageViewCreated(savedInstanceState: Bundle?)

    fun loadData()
}