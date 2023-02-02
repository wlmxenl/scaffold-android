package com.wlmxenl.scaffold.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.wlmxenl.scaffold.R

/**
 * Navigation 扩展, 添加默认动画支持
 * @author wangzf
 * @date 2022/2/13
 */

fun Fragment.scaffoldNavigate(@IdRes actionId: Int, bundle: Bundle? = null, navOptions: NavOptions? = null) {
    val navController = findNavController()
    var destNavOptions = navOptions
    // 从 navigation 配置读取
    if (destNavOptions == null) {
        destNavOptions = navController.currentDestination?.getAction(actionId)?.navOptions
    }
    if (destNavOptions == null) {
        destNavOptions = NavOptions.Builder().build()
    }
    // 修改动画配置
    val newNavOptions = destNavOptions.let {
        navOptions {
            anim {
                enter = if (it.enterAnim == -1) R.anim.scaffold_slide_in_right else it.enterAnim
                exit = if (it.exitAnim == -1) R.anim.scaffold_slide_out_left else it.exitAnim
                popEnter = if (it.popEnterAnim == -1) R.anim.scaffold_slide_in_left else it.popEnterAnim
                popExit = if (it.popExitAnim == -1) R.anim.scaffold_slide_out_right else it.popExitAnim
            }

            // navigation 2.4.0 以上版本
            if (it.popUpToRoute != null) {
                popUpTo(it.popUpToRoute!!) {
                    inclusive = it.isPopUpToInclusive()
                    saveState = it.shouldPopUpToSaveState()
                }
            } else {
                popUpTo(it.popUpToId) {
                    inclusive = it.isPopUpToInclusive()
                    saveState = it.shouldPopUpToSaveState()
                }
            }
            launchSingleTop = it.shouldLaunchSingleTop()
            restoreState = it.shouldRestoreState()
        }
    }
    navController.navigate(actionId, bundle, newNavOptions)
}
