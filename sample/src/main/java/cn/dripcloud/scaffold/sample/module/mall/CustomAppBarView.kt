package cn.dripcloud.scaffold.sample.module.mall

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import cn.dripcloud.scaffold.sample.R
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

/**
 *
 * @Author wangzhengfu
 * @Date 2022/2/11
 */
class CustomAppBarView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    init {
        setBackgroundResource(R.color.appbar_background_color)
        setPadding(0, UltimateBarX.getStatusBarHeight(), 0, 0)
        inflate(context, R.layout.sample_custom_appbar_view, this)
    }
}