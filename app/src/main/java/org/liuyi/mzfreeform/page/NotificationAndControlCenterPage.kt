package org.liuyi.mzfreeform.page

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mzfreeform.DataConst

/**
 * @Author: Liuyi
 * @Date: 2023/04/21/19:45:22
 * @Description:
 */
@BMPage("NotificationAndControlCenterPage", "通知和控制中心")
class NotificationAndControlCenterPage : BasePage() {
    override fun onCreate() {
        TitleText(text = "基础")
        TextSummaryWithSwitch(
            TextSummaryV("打开通知", tips = "单击通知使用小窗打开"),
            createSwitchV(DataConst.OPEN_NOTICE)
        )
        TextSummaryWithSwitch(
            TextSummaryV("长按快捷开关", tips = "使用小窗打开控制中心的快捷方式，默认强制禁用小窗黑名单"),
            createSwitchV(DataConst.LONG_PRESS_TILE)
        )

        Line()
        TitleText(text = "扩展")
        TextSummaryWithSwitch(
            TextSummaryV("强制所有小窗打开", tips = "在上面的基础上，尽量全部使用小窗打开，哪怕不符合操作逻辑"),
            createSwitchV(DataConst.FORCE_CONTROL_ALL_OPEN)
        )
        TextSummaryWithSwitch(
            TextSummaryV("解除小窗展开通知限制"),
            createSwitchV(DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW)
        )
    }

    private fun createSwitchV(prefsData: PrefsData<Boolean>) =
        SwitchV(prefsData.key, activity.prefs().get(prefsData)) {
            activity.prefs().edit { put(prefsData, it) }
        }
}