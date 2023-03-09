package com.wlmxenl.scaffold.brvah

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wlmxenl.scaffold.pagination.PaginationCallback
import com.wlmxenl.scaffold.pagination.PaginationRequest
import com.wlmxenl.scaffold.pagination.PaginationState
import com.wlmxenl.scaffold.stateview.IMultiStateView
import com.wlmxenl.scaffold.stateview.ViewState

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class BrvahPaginationHelper<T> private constructor(builder: Builder<T>): OnRefreshListener, OnLoadMoreListener {
    private val pagingRequest: PaginationRequest<T> = builder.pagingRequest
    private val pagingCallback: PaginationCallback? = builder.pagingCallback
    private val pageStateView: IMultiStateView? = builder.pageStateView
    private val refreshLayout: RefreshLayout? = builder.refreshLayout
    private val rvList: RecyclerView = builder.recyclerView
    private val rvAdapter: BaseQuickAdapter<T, BaseViewHolder> = builder.adapter
    var mState = PaginationState.ON_LOAD_FIRST_PAGE_DATA
        private set
    var isRequesting = false // 防止数据填充不满一屏 并且 可以继续翻页时，会触发2次 onLoadMore
        private set
    var showLoadMoreEndView = builder.showLoadMoreEndView // 分页结束后是否显示没有更多数据

    init {
        refreshLayout?.setOnRefreshListener(this)
        rvAdapter.loadMoreModule.setOnLoadMoreListener(this)
        rvList.adapter = rvAdapter
    }

    /**
     * 下拉刷新回调
     * @param refreshLayout
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        // 加载更多时拦截下拉刷新操作
        if (isRequesting) {
            refreshLayout.finishRefresh()
            return
        }
        mState = PaginationState.ON_LOAD_FIRST_PAGE_DATA
        loadData()
    }

    /**
     * 加载更多回调
     */
    override fun onLoadMore() {
        if (isRequesting || mState == PaginationState.ON_PAGING_FINISHED) {
            return
        }
        mState = PaginationState.ON_LOAD_NEXT_PAGE_DATA
        loadData()
    }

    fun loadData() {
        isRequesting = true
        pagingCallback?.onLoadDataPrepared(mState)

        // 初次加载页面显示 Loading 状态
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA && rvAdapter.data.isEmpty()) {
            pageStateView?.setState(ViewState.LOADING)
        }
        // 执行分页数据请求
        rvList.post {
            pagingRequest.loadData(mState, object : PaginationRequest.Callback<T> {
                override fun onResult(
                    pagingFinished: Boolean,
                    rawData: Any,
                    result: Result<List<T>>
                ) {
                    pagingCallback?.onLoadDataCompleted(rawData, mState)
                    fillData(result, pagingFinished)
                    isRequesting = false
                }
            })
        }
    }

    private fun fillData(result: Result<List<T>>, pagingFinished: Boolean) {
        // 结束下拉刷新动画
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA && refreshLayout?.isRefreshing == true) {
            refreshLayout.finishRefresh()
        }
        // 请求数据失败
        if (result.isFailure) {
            onLoadDataFailed()
            return
        }
        // 请求数据成功
        onLoadDataSucceeded(result, pagingFinished)
    }

    private fun onLoadDataSucceeded(result: Result<List<T>>, pagingFinished: Boolean) {
        // 填充列表数据
        val dataList = result.getOrElse { emptyList() }
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA) {
            rvAdapter.setNewInstance(dataList.toMutableList())
        } else {
            rvAdapter.addData(dataList)
        }

        // 修改 LoadMoreView 样式
        rvList.post {
            rvAdapter.loadMoreModule.let {
                if (pagingFinished) it.loadMoreEnd(!showLoadMoreEndView) else it.loadMoreComplete()
            }
        }

        // 设置页面状态
        val newPageSate = if (rvAdapter.data.isEmpty()) ViewState.EMPTY else ViewState.CONTENT
        if (pageStateView?.getState() != newPageSate) {
            pageStateView?.setState(newPageSate)
        }

        // 修改分页状态
        mState = if (pagingFinished) PaginationState.ON_PAGING_FINISHED else PaginationState.ON_LOAD_NEXT_PAGE_DATA

        // 数据填充完成回调
        pagingCallback?.onFillDataCompleted(mState)
    }

    private fun onLoadDataFailed() {
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA || rvAdapter.data.isEmpty()) {
            pageStateView?.setState(ViewState.ERROR)
        } else {
            rvAdapter.loadMoreModule.loadMoreFail()
        }
        pagingCallback?.onFillDataCompleted(mState)
    }

    /**
     * 设置初始数据
     *
     * @param dataList
     * @param hasLoadMore
     */
    fun setData(dataList: MutableList<T>, hasLoadMore: Boolean) {
        rvAdapter.setNewInstance(dataList)
        rvAdapter.loadMoreModule.let {
            if (hasLoadMore) {
                it.loadMoreComplete()
            } else {
                it.loadMoreEnd()
            }
        }
    }

    class Builder<T> {
        internal lateinit var pagingRequest: PaginationRequest<T>
        internal var pagingCallback: PaginationCallback? = null
        internal var pageStateView: IMultiStateView? = null
        internal var refreshLayout: RefreshLayout? = null
        internal lateinit var recyclerView: RecyclerView
        internal lateinit var adapter: BaseQuickAdapter<T, BaseViewHolder>
        internal var showLoadMoreEndView = true

        fun setPagingRequest(request: PaginationRequest<T>) = apply {
            this.pagingRequest = request
        }

        fun setPagingCallback(callback: PaginationCallback) = apply {
            this.pagingCallback = callback
        }

        fun bindView(refreshLayout: RefreshLayout?, recyclerView: RecyclerView, pageStateView: IMultiStateView?) = apply {
            this.recyclerView = recyclerView
            this.refreshLayout = refreshLayout
            this.pageStateView = pageStateView
        }

        fun setAdapter(adapter: BaseQuickAdapter<T, BaseViewHolder>) = apply {
            this.adapter = adapter
        }

        fun setShowLoadMoreEndView(boolean: Boolean) = apply {
            this.showLoadMoreEndView = boolean
        }

        fun build(): BrvahPaginationHelper<T> = BrvahPaginationHelper(this)
    }
}