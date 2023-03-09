package com.wlmxenl.scaffold.pagination

/**
 * 分页回调
 * @author wangzf
 * @date 2022/4/16
 */
interface PaginationCallback {
    /**
     * 准备请求数据前回调
     * @param paginationState 当前分页状态
     */
    fun onLoadDataPrepared(paginationState: PaginationState)

    /**
     * 请求数据完成后回调
     * @param rawData 接口返回原始数据
     * @param paginationState 当前分页状态
     */
    fun onLoadDataCompleted(rawData: Any?, paginationState: PaginationState)

    /**
     * 数据填充完成后回调
     * @param paginationState 填充数据前分页状态
     */
    fun onFillDataCompleted(paginationState: PaginationState)
}