package com.wlmxenl.scaffold.paging

/**
 * 分页回调
 * @author wangzf
 * @date 2022/4/16
 */
interface IPagingCallback {
    /**
     * 准备请求数据前回调
     * @param pagingState 当前分页状态
     */
    fun onLoadDataPrepared(pagingState: PagingState)

    /**
     * 请求数据完成后回调
     * @param rawData 接口返回原始数据
     * @param pagingState 当前分页状态
     */
    fun onLoadDataCompleted(rawData: Any?, pagingState: PagingState)

    /**
     * 数据填充完成后回调
     * @param pagingState 填充数据前分页状态
     */
    fun onFillDataCompleted(pagingState: PagingState)
}