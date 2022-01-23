package cn.dripcloud.scaffold.sample.base

import android.content.Context
import android.widget.RelativeLayout
import cn.dripcloud.scaffold.page.IAppBarView
import cn.dripcloud.scaffold.sample.R
import cn.dripcloud.scaffold.sample.databinding.SampleAppbarViewBinding
import com.blankj.utilcode.util.BarUtils

/**
 * 应用栏示例
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
class SampleAppBarView(context: Context) : RelativeLayout(context), IAppBarView<SampleAppBarView> {
    val binding: SampleAppbarViewBinding

    init {
        setPadding(0, BarUtils.getActionBarHeight(), 0, 0)
        setBackgroundResource(R.color.appbar_background_color)

        binding = inflate(context, R.layout.sample_appbar_view, this).run {
            SampleAppbarViewBinding.bind(this)
        }
    }

    override fun getContentView() = this
}