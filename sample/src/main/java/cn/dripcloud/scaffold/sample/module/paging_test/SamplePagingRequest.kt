package cn.dripcloud.scaffold.sample.module.paging_test

import androidx.lifecycle.LifecycleCoroutineScope
import cn.dripcloud.scaffold.arch.paging.IPagingRequest
import cn.dripcloud.scaffold.arch.paging.PagingState
import cn.dripcloud.scaffold.sample.BuildConfig
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import rxhttp.await
import rxhttp.toClass
import rxhttp.wrapper.param.RxHttp

/**
 * 玩 Android 首页文章列表数据请求
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingRequest(private val scope: LifecycleCoroutineScope) : IPagingRequest<Any> {
    private var curPage = 1

    override fun loadData(
        pagingState: PagingState,
        callback: IPagingRequest.Callback<Any>
    ) {
        // 重置页码
        if (pagingState == PagingState.ON_LOAD_FIRST_PAGE_DATA) {
            curPage = 1
        }

        scope.launch {
            val requestUrl = "https://www.wanandroid.com/article/list/$curPage/json?page_size=20"

            val rawData = RxHttp.get(requestUrl)
                .toClass<JsonObject>()
                .await {
                    if (BuildConfig.DEBUG) {
                        it.printStackTrace()
                    }
                    JsonObject()
                }

            val result = kotlin.runCatching {
                val dataObj = rawData.getAsJsonObject("data")
                GsonUtils.getGson().fromJson<List<SamplePagingItem>>(dataObj.get("datas"),
                    GsonUtils.getListType(SamplePagingItem::class.java))
            }.onSuccess {
                curPage++
            }

            val isPagingFinished = result.getOrNull().let {
                it.isNullOrEmpty() || curPage > 3
            }

            callback.onResult(isPagingFinished, rawData, result)
        }
    }
}