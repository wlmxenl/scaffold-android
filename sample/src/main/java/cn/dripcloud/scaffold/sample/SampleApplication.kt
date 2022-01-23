package cn.dripcloud.scaffold.sample

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * Sample Application
 * @Author wangzhengfu
 * @Date 2022/1/23
 */
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}