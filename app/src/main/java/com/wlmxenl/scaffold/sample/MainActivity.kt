package com.wlmxenl.scaffold.sample

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wlmxenl.scaffold.sample.base.SampleAppBarView
import com.wlmxenl.scaffold.sample.base.SampleBaseActivity
import com.blankj.utilcode.util.LogUtils
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.ActivityMainBinding
import com.wlmxenl.scaffold.statelayout.StateLayoutDelegate

class MainActivity : SampleBaseActivity<ActivityMainBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        // 重写监听导航事件，去默认除切换动画
        binding.bottomNav.setOnItemSelectedListener {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build()
            navController.navigate(it.itemId, null, navOptions)
            true
        }
    }

    override fun loadData() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        LogUtils.d(item)
        return super.onOptionsItemSelected(item)
    }

    // 首页不需要展示顶部应用栏和多状态布局
    override fun onCreateAppBarView(): SampleAppBarView? = null
    override fun getStateLayoutDelegate(): StateLayoutDelegate? = null

}