package com.wlmxenl.scaffold.sample.features.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.ViewPagerDefaultItemLayoutBinding
import com.wlmxenl.scafflod.sample.databinding.ViewPagerDefaultItemPageLayoutBinding
import com.wlmxenl.scaffold.ext.setDefaultItemIndex
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scaffold.stateview.IMultiStateView
import com.wlmxenl.scaffold.widget.SimpleFragmentStatePagerAdapter

class ViewPagerDefaultItemFragment : SampleBaseFragment<ViewPagerDefaultItemLayoutBinding>() {

    private val defaultIndex = 2
    private val loadDataOutputs = mutableListOf<String>()

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setup {
            setTitle(R.string.sample_view_pager_default_item)
        }

        val fragments = MutableList<Fragment>(4) { index ->
            PageFragment.newInstance(index, index == defaultIndex)
        }
        val fragmentAdapter = SimpleFragmentStatePagerAdapter(childFragmentManager, fragments)

        binding.viewPager.setDefaultItemIndex(defaultIndex)
        binding.viewPager.offscreenPageLimit = fragments.size
        binding.viewPager.adapter = fragmentAdapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                updateCurrentItem(position)
            }
        })
        updateCurrentItem(binding.viewPager.currentItem)
        updateLoadDataOutput()
    }

    fun onPageLoadData(index: Int) {
        loadDataOutputs.add("Page $index loadData()")
        updateLoadDataOutput()
    }

    private fun updateCurrentItem(position: Int) {
        binding.tvCurrentItem.text = "currentItem = $position, defaultIndex = $defaultIndex"
    }

    private fun updateLoadDataOutput() {
        binding.tvLoadDataOutput.text = if (loadDataOutputs.isEmpty()) {
            "loadData output: waiting"
        } else {
            "loadData output:\n${loadDataOutputs.joinToString(separator = "\n")}"
        }
    }

    class PageFragment : SampleBaseFragment<ViewPagerDefaultItemPageLayoutBinding>() {

        private val index: Int
            get() = requireArguments().getInt(KEY_INDEX)

        private val isDefault: Boolean
            get() = requireArguments().getBoolean(KEY_IS_DEFAULT)

        override fun onPageViewCreated(savedInstanceState: Bundle?) {
            binding.pageContainer.setBackgroundColor(
                if (isDefault) {
                    0xFFE8F5E9.toInt()
                } else {
                    0xFFFFFFFF.toInt()
                }
            )
            binding.tvPageTitle.text = if (isDefault) {
                "Page $index\nDefault Item"
            } else {
                "Page $index"
            }
            binding.tvLoadData.text = "loadData(): not called"
        }

        override fun loadData() {
            val output = "Page $index loadData()"
            Log.d(TAG, output)
            binding.tvLoadData.text = "loadData(): called"
            (parentFragment as? ViewPagerDefaultItemFragment)?.onPageLoadData(index)
        }

        override fun onCreateAppBarView(): View? = null

        override fun onCreateMultiStateView(): IMultiStateView? = null

        companion object {
            private const val TAG = "ViewPagerDefaultItem"
            private const val KEY_INDEX = "index"
            private const val KEY_IS_DEFAULT = "is_default"

            fun newInstance(index: Int, isDefault: Boolean): PageFragment {
                return PageFragment().apply {
                    arguments = Bundle().apply {
                        putInt(KEY_INDEX, index)
                        putBoolean(KEY_IS_DEFAULT, isDefault)
                    }
                }
            }
        }
    }
}
