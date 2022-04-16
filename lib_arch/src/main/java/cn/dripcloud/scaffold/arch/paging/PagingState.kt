package cn.dripcloud.scaffold.arch.paging

/**
 * [PagingExecutor] 执行状态
 * @author wangzf
 * @date 2022/4/16
 */
enum class PagingState {
    ON_LOAD_FIRST_PAGE_DATA,
    ON_LOAD_NEXT_PAGE_DATA,
    ON_PAGING_FINISHED
}