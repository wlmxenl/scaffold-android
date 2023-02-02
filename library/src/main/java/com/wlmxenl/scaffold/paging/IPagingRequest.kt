package com.wlmxenl.scaffold.paging

/**
 * 数据请求接口
 * @author wangzf
 * @date 2022/4/16
 */
interface IPagingRequest<T> {

    /**
     * 加载分页数据
     *
     * @param pagingState 分页执行状态
     * @return
     */
    fun loadData(pagingState: PagingState, callback: Callback<T>)


    interface Callback<T> {
        /**
         * 分页数据请求数据回调
         *
         * @param pagingFinished 分页是否已结束
         * @param rawData 接口返回原始数据
         * @param result 列表数据
         */
        fun onResult(pagingFinished: Boolean, rawData: Any, result: Result<List<T>>)
    }
}