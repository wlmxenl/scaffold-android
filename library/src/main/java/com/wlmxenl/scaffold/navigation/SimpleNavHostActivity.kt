package com.wlmxenl.scaffold.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.wlmxenl.scaffold.R
import com.wlmxenl.scaffold.base.BaseScaffoldActivity
import com.wlmxenl.scaffold.databinding.ScaffoldNavigationContainerBinding
import com.wlmxenl.scaffold.statelayout.StateLayoutProvider

/**
 * SimpleNavHostActivity
 * @author wangzf
 * @date 2022/3/11
 */
abstract class SimpleNavHostActivity :
    BaseScaffoldActivity<ScaffoldNavigationContainerBinding, View>() {
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): ScaffoldNavigationContainerBinding {
        return ScaffoldNavigationContainerBinding.inflate(inflater)
    }

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.apply {
            val navGraph = navInflater.inflate(getNavGraphResId()).apply {
                if (getCustomStartDestination() != 0) {
                    setStartDestination(getCustomStartDestination())
                }
            }
            setGraph(navGraph, getStartDestinationArgs())
        }
    }

    override fun loadData() {}

    abstract fun getNavGraphResId(): Int

    open fun getCustomStartDestination() = 0

    open fun getStartDestinationArgs(): Bundle? = intent.extras

    override fun onCreateAppBarView(): View? = null

    override fun getStateLayoutProvider(): StateLayoutProvider? = null
}