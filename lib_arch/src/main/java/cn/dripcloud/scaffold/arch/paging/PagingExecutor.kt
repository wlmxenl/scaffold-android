package cn.dripcloud.scaffold.arch.paging

import androidx.recyclerview.widget.RecyclerView
import cn.dripcloud.scaffold.page.IPageStateLayout
import com.drake.brv.BindingAdapter
import com.drake.brv.PageRefreshLayout
import com.drake.brv.utils.models
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class PagingExecutor<T> private constructor(builder: Builder<T>) {
    private val TAG = "PagingExecutor"
    private val pagingRequest: IPagingRequest<T> = builder.pagingRequest
    private val pagingCallback: IPagingCallback? = builder.pagingCallback
    private val pageStateView: IPageStateLayout? = builder.pageStateView
    private val refreshLayout: PageRefreshLayout = builder.refreshLayout
    private val rvList: RecyclerView = builder.recyclerView
    var mState = PagingState.ON_LOAD_FIRST_PAGE_DATA
        private set

    init {
        refreshLayout.onRefresh {
            doRefresh(this)
        }
        refreshLayout.onLoadMore {
            doLoadMore(this)
        }
    }

    /**
     * 下拉刷新回调
     * @param refreshLayout
     */
    private fun doRefresh(refreshLayout: RefreshLayout) {
        mState = PagingState.ON_LOAD_FIRST_PAGE_DATA
        loadData()
    }

    /**
     * 加载更多回调
     */
    private fun doLoadMore(refreshLayout: RefreshLayout) {
        if (mState == PagingState.ON_PAGING_FINISHED) {
            return
        }
        mState = PagingState.ON_LOAD_NEXT_PAGE_DATA
        loadData()
    }

    private fun loadData() {
        pagingCallback?.onLoadDataPrepared(mState)

        // 初次加载页面显示 Loading 状态
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA && rvList.models.isNullOrEmpty()) {
            pageStateView?.setPageState(IPageStateLayout.STATE_LOADING)
        }

        // 执行分页数据请求
        pagingRequest.loadData(mState, object : IPagingRequest.Callback<T> {
            override fun onResult(
                pagingFinished: Boolean,
                rawData: Any,
                result: Result<List<T>>
            ) {
                pagingCallback?.onLoadDataCompleted(rawData, mState)
                fillData(result, pagingFinished)
            }
        })
    }

    private fun fillData(result: Result<List<T>>, pagingFinished: Boolean) {
        // 结束下拉刷新动画
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA && refreshLayout.isRefreshing) {
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
        refreshLayout.addData(dataList) { !pagingFinished }

        // 设置页面状态
        val newPageSate = if (rvList.models.isNullOrEmpty()) IPageStateLayout.STATE_EMPTY else IPageStateLayout.STATE_CONTENT
        if (pageStateView?.getPageState() != newPageSate) {
            pageStateView?.setPageState(newPageSate)
        }

        // 修改分页状态
        mState = if (pagingFinished) PagingState.ON_PAGING_FINISHED else PagingState.ON_LOAD_NEXT_PAGE_DATA

        // 数据填充完成回调
        pagingCallback?.onFillDataCompleted(mState)
    }

    private fun onLoadDataFailed() {
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA || rvList.models.isNullOrEmpty()) {
            pageStateView?.setPageState(IPageStateLayout.STATE_ERROR)
        } else {
            refreshLayout.finishLoadMore(0, success = false, noMoreData = false)
        }
        pagingCallback?.onFillDataCompleted(mState)
    }


    /**
     * 加载第一页数据
     */
    fun loadFirstPageData() {
        refreshLayout.refresh()
    }

    /**
     * 设置初始数据
     *
     * @param dataList
     * @param hasLoadMore
     */
    fun setData(dataList: MutableList<T>, hasLoadMore: Boolean) {
        refreshLayout.addData(dataList) { hasLoadMore }
    }

    class Builder<T> {
        internal lateinit var pagingRequest: IPagingRequest<T>
        internal var pagingCallback: IPagingCallback? = null
        internal var pageStateView: IPageStateLayout? = null
        internal lateinit var refreshLayout: PageRefreshLayout
        internal lateinit var recyclerView: RecyclerView
        // internal lateinit var adapter: BindingAdapter
        internal var showLoadMoreEndView = true

        fun setPagingRequest(request: IPagingRequest<T>) = apply {
            this.pagingRequest = request
        }

        fun setPagingCallback(callback: IPagingCallback) = apply {
            this.pagingCallback = callback
        }

        fun bindView(refreshLayout: PageRefreshLayout, recyclerView: RecyclerView, pageStateView: IPageStateLayout?) = apply {
            this.recyclerView = recyclerView
            this.refreshLayout = refreshLayout
            this.pageStateView = pageStateView
        }

        fun setAdapter(adapter: BindingAdapter) = apply {
            // this.adapter = adapter
        }

        fun setShowLoadMoreEndView(boolean: Boolean) = apply {
            this.showLoadMoreEndView = boolean
        }

        fun build(): PagingExecutor<T> = PagingExecutor(this)
    }
}