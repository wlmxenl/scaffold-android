//package cn.dripcloud.scaffold.sample.module.paging_test
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import cn.dripcloud.scaffold.sample.databinding.PagingRecycleItemBinding
//import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
//
///**
// *
// * @author wangzf
// * @date 2022/4/16
// */
//class SamplePagingItemBinder : QuickViewBindingItemBinder<SamplePagingItem, PagingRecycleItemBinding>() {
//
//    override fun convert(holder: BinderVBHolder<PagingRecycleItemBinding>, data: SamplePagingItem) {
//        holder.viewBinding.run {
//            tvTitle.text = data.title
//            tvCreateTime.text = data.niceShareDate
//            tvShareUser.text = data.shareUser
//        }
//    }
//
//    override fun onCreateViewBinding(
//        layoutInflater: LayoutInflater,
//        parent: ViewGroup,
//        viewType: Int
//    ): PagingRecycleItemBinding {
//        return PagingRecycleItemBinding.inflate(layoutInflater, parent, false)
//    }
//}