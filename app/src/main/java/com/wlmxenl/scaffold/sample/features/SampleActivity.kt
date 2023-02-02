package com.wlmxenl.scaffold.sample.features

import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scaffold.sample.base.BaseNavHostActivity

/**
 *
 * @author wangzf
 * @date 2022/8/18
 */
class SampleActivity : BaseNavHostActivity() {

    override fun getNavGraphResId(): Int {
        return R.navigation.nav_graph
    }

    override fun getCustomStartDestination(): Int {
        return intent.getIntExtra("startDestination", 0)
    }
}