@file:JvmName("TextViewExtKt")
package com.wlmxenl.scaffold.ext

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun TextView.setTypefaceStyle(style: Int) {
    val safeStyle = when (style) {
        Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC -> style
        else -> Typeface.NORMAL
    }
    typeface = Typeface.defaultFromStyle(safeStyle)
}

fun TextView.setDrawableRight(drawableId: Int, width: Int = 0, height: Int = 0) {
    setCompoundDrawables(null, null, generateCompoundDrawable(drawableId, width, height), null)
}

fun TextView.setDrawableLeft(drawableId: Int, width: Int = 0, height: Int = 0) {
    setCompoundDrawables(generateCompoundDrawable(drawableId, width, height), null, null, null)
}

fun TextView.setDrawableTop(drawableId: Int, width: Int = 0, height: Int = 0) {
    setCompoundDrawables(null, generateCompoundDrawable(drawableId, width, height), null, null)
}

fun TextView.setDrawableBottom(drawableId: Int, width: Int = 0, height: Int = 0) {
    setCompoundDrawables(null, null, null, generateCompoundDrawable(drawableId, width, height))
}

fun TextView.generateCompoundDrawable(@DrawableRes resId: Int, width: Int, height: Int): Drawable? {
    return ContextCompat.getDrawable(context, resId)?.apply {
        setBounds(
            0,
            0,
            if (width <= 0) minimumWidth else width,
            if (height <= 0) minimumHeight else height
        )
    }
}
