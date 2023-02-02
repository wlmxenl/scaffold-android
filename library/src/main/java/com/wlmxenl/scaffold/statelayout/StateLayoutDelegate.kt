package com.wlmxenl.scaffold.statelayout

import android.view.View

abstract class StateLayoutDelegate : IStateLayout {

    abstract fun convertContentView(contentView: View): View

    abstract fun getStateLayout(): View?
}