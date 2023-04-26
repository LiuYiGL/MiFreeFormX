package org.liuyi.mzfreeform.activity.page

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import org.liuyi.mzfreeform.DataConst

/**
 * @Author: Liuyi
 * @Date: 2023/04/23/17:45:52
 * @Description:
 */
@BMPage("ShareToAppPage", "小窗分享")
class ShareToAppPage : MyBasePage() {
    override fun onCreate() {
        TitleText(text = "基础")
        TextSummaryWithSwitch(
            TextSummaryV("分享至应用", tips = "在应用中分享时使用小窗打开"),
            createSwitchV(DataConst.SHARE_TO_APP)
        )

        Line()
        TitleText(text = "扩展")
        TextSummaryWithSwitch(
            TextSummaryV("分享时强制使用新窗口打开", tips = "增强分享至应用"),
            createSwitchV(DataConst.SHARE_TO_APP_FORCE_NEW_TASK)
        )
    }
}