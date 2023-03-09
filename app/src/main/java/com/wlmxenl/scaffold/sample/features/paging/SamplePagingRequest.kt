package com.wlmxenl.scaffold.sample.features.paging

import androidx.lifecycle.LifecycleOwner
import com.wlmxenl.scaffold.pagination.PaginationRequest
import com.wlmxenl.scaffold.pagination.PaginationState
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
class SamplePagingRequest(private val lifecycleOwner: LifecycleOwner) : PaginationRequest<Any> {
    private var curPage = 1
    private var maxPage = 4

    override fun loadData(
        paginationState: PaginationState,
        callback: PaginationRequest.Callback<Any>
    ) {
        // 重置页码
        if (paginationState == PaginationState.ON_LOAD_FIRST_PAGE_DATA) {
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
            val isPagingFinished = result.isSuccess && curPage >= maxPage
            callback.onResult(isPagingFinished, rawData, result)
        }
    }
}