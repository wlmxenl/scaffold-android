package com.wlmxenl.scaffold.sample.features.statelayout

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scafflod.sample.databinding.SampleStateLayoutBinding
import com.wlmxenl.scaffold.statelayout.IStateLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StateLayoutSampleFragment : SampleBaseFragment<SampleStateLayoutBinding>() {

    override fun onPageViewCreated(savedInstanceState: Bundle?) {

        binding.btnLoading.setOnClickListener {
            stateLayout?.setState(IStateLayout.LOADING)
            showContentView()
        }

        binding.btnEmpty.setOnClickListener {
            stateLayout?.setState(IStateLayout.EMPTY)
            showContentView()
        }

        binding.btnError.setOnClickListener {
            stateLayout?.setState(IStateLayout.ERROR)
            showContentView()
        }

        binding.btnContent.setOnClickListener {
            stateLayout?.setState(IStateLayout.CONTENT)
        }
    }

    private fun showContentView() {
        lifecycleScope.launch {
            delay(2000)
            stateLayout?.setState(IStateLayout.CONTENT)
        }
    }
}