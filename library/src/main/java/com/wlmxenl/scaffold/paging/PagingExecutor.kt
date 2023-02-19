package com.wlmxenl.scaffold.paging

import androidx.recyclerview.widget.RecyclerView
import com.wlmxenl.scaffold.paging.loadState.LoadState
import com.wlmxenl.scaffold.paging.loadState.trailing.DefaultTrailingLoadStateAdapter
import com.wlmxenl.scaffold.paging.loadState.trailing.TrailingLoadStateAdapter
import com.wlmxenl.scaffold.statelayout.IStateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class PagingExecutor<T> private constructor(builder: Builder<T>): OnRefreshListener {
    private val pagingRequest: IPagingRequest<T> = builder.pagingRequest
    private val pagingCallback: IPagingCallback? = builder.pagingCallback
    private val pageStateView: IStateLayout? = builder.pageStateView
    private val refreshLayout: SmartRefreshLayout? = builder.refreshLayout
    private val rvList: RecyclerView = builder.recyclerView
    private val helper: QuickAdapterHelper
    private var isRequesting = false
    var mState = PagingState.ON_LOAD_FIRST_PAGE_DATA
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
                    return mState != PagingState.ON_PAGING_FINISHED
                }
            })
        rvList.adapter = helper.adapter
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadFirstPageData()
    }

    /**
     * 加载更多回调
     */
    private fun doLoadMore() {
        if (isRequesting || mState == PagingState.ON_PAGING_FINISHED) {
            return
        }
        mState = PagingState.ON_LOAD_NEXT_PAGE_DATA
        loadData()
    }

    private fun loadData() {
        isRequesting = true
        pagingCallback?.onLoadDataPrepared(mState)

        // 初次加载页面显示 Loading 状态
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA && helper.contentAdapter.models.isNullOrEmpty()) {
            pageStateView?.setState(IStateLayout.LOADING)
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
                isRequesting = false
            }
        })
    }

    private fun fillData(result: Result<List<T>>, pagingFinished: Boolean) {
        // 结束下拉刷新动画
        refreshLayout?.let {
            if (it.isRefreshing && mState == PagingState.ON_LOAD_FIRST_PAGE_DATA) {
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
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA) {
            setNewData(dataList)
        } else {
            helper.contentAdapter.addModels(dataList)
        }

        // 修改 LoadMoreView 样式
        setTrailingLoadState(!pagingFinished)

        // 设置页面状态
        val newPageSate = if (helper.contentAdapter.models.isNullOrEmpty()) IStateLayout.EMPTY else IStateLayout.CONTENT
        if (pageStateView?.getState() != newPageSate) {
            pageStateView?.setState(newPageSate)
        }

        // 修改分页状态
        mState = if (pagingFinished) PagingState.ON_PAGING_FINISHED else PagingState.ON_LOAD_NEXT_PAGE_DATA

        // 数据填充完成回调
        pagingCallback?.onFillDataCompleted(mState)
    }

    private fun onLoadDataFailed(error: Throwable) {
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA || helper.contentAdapter.models.isNullOrEmpty()) {
            pageStateView?.setState(IStateLayout.ERROR)
        } else {
            helper.trailingLoadState = LoadState.Error(error)
        }
        pagingCallback?.onFillDataCompleted(mState)
    }

    /**
     * 加载第一页数据
     */
    fun loadFirstPageData() {
        mState = PagingState.ON_LOAD_FIRST_PAGE_DATA
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

    val pagingAdapterHelper = helper

    class Builder<T> {
        internal lateinit var pagingRequest: IPagingRequest<T>
        internal var pagingCallback: IPagingCallback? = null
        internal var pageStateView: IStateLayout? = null
        internal var refreshLayout: SmartRefreshLayout? = null
        internal lateinit var recyclerView: RecyclerView
        internal lateinit var adapter: ScaffoldPagingAdapter
        internal lateinit var trailingLoadStateAdapter: TrailingLoadStateAdapter<*>

        fun setPagingRequest(request: IPagingRequest<T>) = apply {
            this.pagingRequest = request
        }

        fun setPagingCallback(callback: IPagingCallback) = apply {
            this.pagingCallback = callback
        }

        fun bindView(refreshLayout: SmartRefreshLayout?, recyclerView: RecyclerView, pageStateView: IStateLayout?) = apply {
            this.refreshLayout = refreshLayout
            this.recyclerView = recyclerView
            this.pageStateView = pageStateView
        }

        fun setAdapter(adapter: ScaffoldPagingAdapter) = apply {
            this.adapter = adapter
        }

        fun setCustomTrailingLoadStateAdapter(stateAdapter: TrailingLoadStateAdapter<*>) {
            this.trailingLoadStateAdapter = stateAdapter
        }

        fun build(): PagingExecutor<T> {
            if (!::trailingLoadStateAdapter.isInitialized) {
                setCustomTrailingLoadStateAdapter(DefaultTrailingLoadStateAdapter())
            }
            return PagingExecutor(this)
        }
    }
}