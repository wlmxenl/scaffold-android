package cn.dripcloud.scaffold.arch.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * 支持懒加载的 FragmentStatePagerAdapter
 *
 * Fragment 懒加载参考 [cn.dripcloud.scaffold.page.BasePageFragment.onResume]
 *
 * @author wangzf
 * @date 2022/3/1
 *
 */
open class SimpleFragmentStatePagerAdapter(fm: FragmentManager, private val fragments: MutableList<Fragment>)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}