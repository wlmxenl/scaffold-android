package com.wlmxenl.scaffold.statelayout

import android.view.View

abstract class StateLayoutProvider : IStateLayout {

    abstract fun convertContentView(contentView: View): View

    abstract fun getStateLayout(): View?
}