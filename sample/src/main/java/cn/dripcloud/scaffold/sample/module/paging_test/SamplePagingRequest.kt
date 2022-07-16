package cn.dripcloud.scaffold.sample.module.paging_test

import androidx.lifecycle.LifecycleOwner
import cn.dripcloud.scaffold.arch.paging.IPagingRequest
import cn.dripcloud.scaffold.arch.paging.PagingState
import com.blankj.utilcode.util.GsonUtils
import com.drake.net.Get
import com.drake.net.utils.scopeNetLife
import com.drake.net.utils.withIO
import com.google.gson.JsonObject

/**
 * 玩 Android 首页文章列表数据请求
 * @author wangzf
 * @date 2022/4/16
 */
class SamplePagingRequest(private val lifecycleOwner: LifecycleOwner) : IPagingRequest<Any> {
    private var curPage = 1

    override fun loadData(
        pagingState: PagingState,
        callback: IPagingRequest.Callback<Any>
    ) {
        // 重置页码
        if (pagingState == PagingState.ON_LOAD_FIRST_PAGE_DATA) {
            curPage = 1
        }

        val requestUrl = "https://www.wanandroid.com/article/list/$curPage/json?page_size=20"

        lifecycleOwner.scopeNetLife {
            val bodyString = Get<String>(requestUrl).await()
            val rawData = withIO {
                GsonUtils.getGson().fromJson(bodyString, JsonObject::class.java)
            }
            val result = withIO {
                rawData.runCatching {
                    getAsJsonObject("data")
                        .let {
                            GsonUtils.getGson().fromJson<List<SamplePagingItem>>(it.get("datas"),
                                GsonUtils.getListType(SamplePagingItem::class.java))
                        }
                }.onSuccess {
                    curPage++
                }
            }
            val isPagingFinished = result.isSuccess && result.getOrThrow().isEmpty()
            callback.onResult(isPagingFinished, rawData, result)
        }
    }
}