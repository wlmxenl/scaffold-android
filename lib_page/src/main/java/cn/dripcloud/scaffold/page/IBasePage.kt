package cn.dripcloud.scaffold.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface IBasePage<VB : ViewBinding, APPBAR: IAppBarView<out View>> {
    fun initData(savedInstanceState: Bundle?)
    fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): VB
    fun onCreateAppBarView(): APPBAR?
    fun onCreatePageStateView(): IPageStateView?
    fun initView()
    fun loadData()
}