package cn.dripcloud.scaffold.page

import android.view.View

interface IAppBarView<V : View> {

    fun getContentView(): V

}