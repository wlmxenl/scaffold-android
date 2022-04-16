package cn.dripcloud.scaffold.arch.paging

import androidx.recyclerview.widget.RecyclerView
import cn.dripcloud.scaffold.page.IPageStateView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 *
 * @author wangzf
 * @date 2022/4/16
 */
class PagingExecutor<T> private constructor(builder: Builder<T>): OnRefreshListener, OnLoadMoreListener {
    private val pagingRequest: IPagingRequest<T> = builder.pagingRequest
    private val pagingCallback: IPagingCallback? = builder.pagingCallback
    private val pageStateView: IPageStateView? = builder.pageStateView
    private val refreshLayout: RefreshLayout? = builder.refreshLayout
    private val rvList: RecyclerView = builder.recyclerView
    private val rvAdapter: BaseQuickAdapter<T, BaseViewHolder> = builder.adapter
    var mState = PagingState.ON_LOAD_FIRST_PAGE_DATA
        private set
    var isRequesting = false // 防止数据填充不满一屏 并且 可以继续翻页时，会触发2次 onLoadMore
        private set

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
        mState = PagingState.ON_LOAD_FIRST_PAGE_DATA
        loadData()
    }

    /**
     * 加载更多回调
     */
    override fun onLoadMore() {
        if (isRequesting || mState == PagingState.ON_PAGING_FINISHED) {
            return
        }
        mState = PagingState.ON_LOAD_NEXT_PAGE_DATA
        loadData()
    }

    fun loadData() {
        isRequesting = true
        pagingCallback?.onLoadDataPrepared(mState)

        // 初次加载页面显示 Loading 状态
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA && rvAdapter.data.isEmpty()) {
            pageStateView?.setPageState(IPageStateView.STATE_LOADING)
        }
        // 执行分页数据请求
        rvList.post {
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
    }

    private fun fillData(result: Result<List<T>>, pagingFinished: Boolean) {
        // 结束下拉刷新动画
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA && refreshLayout?.isRefreshing == true) {
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
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA) {
            rvAdapter.setNewInstance(dataList.toMutableList())
        } else {
            rvAdapter.addData(dataList)
        }

        // 修改 LoadMoreView 样式
        rvList.post {
            rvAdapter.loadMoreModule.let {
                if (pagingFinished) it.loadMoreEnd() else it.loadMoreComplete()
            }
        }

        // 设置页面状态
        val newPageSate = if (rvAdapter.data.isEmpty()) IPageStateView.STATE_EMPTY else IPageStateView.STATE_CONTENT
        if (pageStateView?.getPageState() != newPageSate) {
            pageStateView?.setPageState(newPageSate)
        }

        // 修改分页状态
        mState = if (pagingFinished) PagingState.ON_PAGING_FINISHED else PagingState.ON_LOAD_NEXT_PAGE_DATA

        // 数据填充完成回调
        pagingCallback?.onFillDataCompleted(mState)
    }

    private fun onLoadDataFailed() {
        if (mState == PagingState.ON_LOAD_FIRST_PAGE_DATA || rvAdapter.data.isEmpty()) {
            pageStateView?.setPageState(IPageStateView.STATE_ERROR)
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
        internal lateinit var pagingRequest: IPagingRequest<T>
        internal var pagingCallback: IPagingCallback? = null
        internal var pageStateView: IPageStateView? = null
        internal var refreshLayout: RefreshLayout? = null
        internal lateinit var recyclerView: RecyclerView
        internal lateinit var adapter: BaseQuickAdapter<T, BaseViewHolder>

        fun setPagingRequest(request: IPagingRequest<T>) = apply {
            this.pagingRequest = request
        }

        fun setPagingCallback(callback: IPagingCallback) = apply {
            this.pagingCallback = callback
        }

        fun bindView(refreshLayout: RefreshLayout?, recyclerView: RecyclerView, pageStateView: IPageStateView?) = apply {
            this.recyclerView = recyclerView
            this.refreshLayout = refreshLayout
            this.pageStateView = pageStateView
        }

        fun setAdapter(adapter: BaseQuickAdapter<T, BaseViewHolder>) = apply {
            this.adapter = adapter
        }

        fun build(): PagingExecutor<T> = PagingExecutor(this)
    }
}