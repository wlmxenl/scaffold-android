package com.wlmxenl.scaffold.pagination

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wlmxenl.scaffold.pagination.loadState.LoadState
import com.wlmxenl.scaffold.pagination.loadState.trailing.DefaultTrailingLoadStateAdapter
import com.wlmxenl.scaffold.pagination.loadState.trailing.TrailingLoadStateAdapter
import com.wlmxenl.scaffold.stateview.IMultiStateView
import com.wlmxenl.scaffold.stateview.ViewState
import com.wlmxenl.scaffold.util.RecyclerViewUtils

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class BrvPaginationHelper<T> private constructor(builder: Builder<T>): OnRefreshListener {
    private val pagingRequest: PaginationRequest<T> = builder.pagingRequest
    private val pagingCallback: PaginationCallback? = builder.pagingCallback
    private val multiStateView: IMultiStateView? = builder.multiStateView
    private val refreshLayout: SmartRefreshLayout? = builder.refreshLayout
    private val rvList: RecyclerView = builder.recyclerView
    private val helper: QuickAdapterHelper
    private var isRequesting = false
    var mState = PaginationState.ON_LOAD_FIRST_PAGE_DATA
        private set

    init {
        refreshLayout?.setOnRefreshListener(this)

        helper = QuickAdapterHelper.Builder(builder.adapter)
            .setTrailingLoadStateAdapter(builder.trailingLoadStateAdapter)
            .build()

        helper.trailingLoadStateAdapter!!
            .setOnLoadMoreListener(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onLoad() {
                    rvList.post {
                        doLoadMore()
                    }
                }

                override fun onFailRetry() {
                    doLoadMore()
                }

                override fun isAllowLoading(): Boolean {
                    return mState != PaginationState.ON_PAGING_FINISHED
                }
            })

        val concatAdapter = helper.adapter as ConcatAdapter
        rvList.adapter = concatAdapter

        // 处理 SpanSizeLookup
        (rvList.layoutManager as? GridLayoutManager)?.run {
            RecyclerViewUtils.setGridFullSpan(concatAdapter, this)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadFirstPageData()
    }

    /**
     * 加载更多回调
     */
    private fun doLoadMore() {
        if (isRequesting || mState == PaginationState.ON_PAGING_FINISHED) {
            return
        }
        mState = PaginationState.ON_LOAD_NEXT_PAGE_DATA
        loadData()
    }

    private fun loadData() {
        isRequesting = true
        pagingCallback?.onLoadDataPrepared(mState)

        // 初次加载页面显示 Loading 状态
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA && helper.contentAdapter.models.isNullOrEmpty()) {
            multiStateView?.setState(ViewState.LOADING)
        }

        // 执行分页数据请求
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

    private fun fillData(result: Result<List<T>>, pagingFinished: Boolean) {
        // 结束下拉刷新动画
        refreshLayout?.let {
            if (it.isRefreshing && mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA) {
                it.finishRefresh()
            }
        }

        // 请求数据失败
        if (result.isFailure) {
            onLoadDataFailed(result.exceptionOrNull()!!)
            return
        }

        // 请求数据成功
        onLoadDataSucceeded(result, pagingFinished)
    }

    private fun setNewData(dataList: List<T>) {
        val models = helper.contentAdapter.models
        if (models == null) {
            helper.contentAdapter.models = dataList
        } else {
            if (models is MutableList) {
                val size = models.size
                models.clear()
                helper.contentAdapter.checkedPosition.clear()
                if (dataList.isEmpty()) {
                    helper.contentAdapter.notifyItemRangeRemoved(helper.contentAdapter.headerCount, size)
                } else {
                    helper.contentAdapter.addModels(dataList)
                }
            }
        }
    }

    private fun onLoadDataSucceeded(result: Result<List<T>>, pagingFinished: Boolean) {
        // 填充列表数据
        val dataList = result.getOrElse { emptyList() }

        // see: [PageRefreshLayout.addData()]
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA) {
            setNewData(dataList)
        } else {
            helper.contentAdapter.addModels(dataList)
        }

        // 修改 LoadMoreView 样式
        setTrailingLoadState(!pagingFinished)

        // 设置页面状态
        val newPageSate = if (helper.contentAdapter.models.isNullOrEmpty()) ViewState.EMPTY else ViewState.CONTENT
        if (multiStateView?.getState() != newPageSate) {
            multiStateView?.setState(newPageSate)
        }

        // 修改分页状态
        mState = if (pagingFinished) PaginationState.ON_PAGING_FINISHED else PaginationState.ON_LOAD_NEXT_PAGE_DATA

        // 数据填充完成回调
        pagingCallback?.onFillDataCompleted(mState)
    }

    private fun onLoadDataFailed(error: Throwable) {
        if (mState == PaginationState.ON_LOAD_FIRST_PAGE_DATA || helper.contentAdapter.models.isNullOrEmpty()) {
            multiStateView?.setState(ViewState.ERROR)
        } else {
            helper.trailingLoadState = LoadState.Error(error)
        }
        pagingCallback?.onFillDataCompleted(mState)
    }

    /**
     * 加载第一页数据
     */
    fun loadFirstPageData() {
        mState = PaginationState.ON_LOAD_FIRST_PAGE_DATA
        helper.trailingLoadState = LoadState.None
        loadData()
    }

    /**
     * 设置初始数据
     *
     * @param dataList
     * @param hasLoadMore
     */
    fun setData(dataList: MutableList<T>, hasLoadMore: Boolean) {
        setNewData(dataList)
        setTrailingLoadState(hasLoadMore)
    }

    private fun setTrailingLoadState(hasLoadMore: Boolean) {
        // !hasLoadMore=false, 设置状态为未加载，并且还有分页数据
        // !hasLoadMore=true, 设置状态为未加载，并且没有分页数据了
        helper.trailingLoadState = LoadState.NotLoading(!hasLoadMore)
    }

    class Builder<T> {
        internal lateinit var pagingRequest: PaginationRequest<T>
        internal var pagingCallback: PaginationCallback? = null
        internal var multiStateView: IMultiStateView? = null
        internal var refreshLayout: SmartRefreshLayout? = null
        internal lateinit var recyclerView: RecyclerView
        internal lateinit var adapter: ScaffoldBrvAdapter
        internal lateinit var trailingLoadStateAdapter: TrailingLoadStateAdapter<*>

        fun setPagingRequest(request: PaginationRequest<T>) = apply {
            this.pagingRequest = request
        }

        fun setPagingCallback(callback: PaginationCallback) = apply {
            this.pagingCallback = callback
        }

        fun bindView(refreshLayout: SmartRefreshLayout?, recyclerView: RecyclerView, multiStateView: IMultiStateView?) = apply {
            this.refreshLayout = refreshLayout
            this.recyclerView = recyclerView
            this.multiStateView = multiStateView
        }

        fun setAdapter(adapter: ScaffoldBrvAdapter) = apply {
            this.adapter = adapter
        }

        fun setCustomTrailingLoadStateAdapter(stateAdapter: TrailingLoadStateAdapter<*>) {
            this.trailingLoadStateAdapter = stateAdapter
        }

        fun build(): BrvPaginationHelper<T> {
            if (!::trailingLoadStateAdapter.isInitialized) {
                setCustomTrailingLoadStateAdapter(DefaultTrailingLoadStateAdapter())
            }
            return BrvPaginationHelper(this)
        }
    }
}