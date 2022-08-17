package cn.dripcloud.scaffold.page

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 内容布局包裹接口
 * @author wangzf
 * @date 2022/8/17
 */
interface IContentLayoutWrapper : IPageStateLayout {

    /**
     * 包裹内容布局
     * @param pageRootLayout 业务页面布局上级布局
     * @param pageContentLayout 业务页面布局
     */
    fun wrap(pageRootLayout: ConstraintLayout, pageContentLayout: View, appBarViewId: Int)
}