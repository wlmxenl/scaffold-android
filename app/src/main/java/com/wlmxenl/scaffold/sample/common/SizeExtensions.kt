package com.wlmxenl.scaffold.sample.common

import com.blankj.utilcode.util.Utils

/**
 *
 * @author wangzf
 * @date 2022/3/30
 */

fun Float.dp2pxFloat(): Float {
    return Utils.getApp().resources.getDimension(com.wildma.swqualifier.R.dimen.dp_1) * this
}

fun Float.dp2pxInt(): Int {
    return (Utils.getApp().resources.getDimension(com.wildma.swqualifier.R.dimen.dp_1) * this).toInt()
}

fun Float.sp2pxFloat(): Float {
    return Utils.getApp().resources.getDimension(com.wildma.swqualifier.R.dimen.sp_1) * this
}

fun Float.sp2pxInt(): Int {
    return (Utils.getApp().resources.getDimension(com.wildma.swqualifier.R.dimen.sp_1) * this).toInt()
}