package cn.dripcloud.scaffold.sample

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.drake.brv.utils.BRV
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * Sample Application
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        BRV.modelId = BR.m
        NetConfig.initialize {
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }

        StateConfig.apply {
            loadingLayout = R.layout.page_state_loading
        }
    }
}