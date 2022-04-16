package cn.dripcloud.scaffold.sample

import android.app.Application
import com.blankj.utilcode.util.Utils
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.param.RxHttp

/**
 * Sample Application
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        RxHttpPlugins.init(null)
            .setDebug(BuildConfig.DEBUG)
    }
}