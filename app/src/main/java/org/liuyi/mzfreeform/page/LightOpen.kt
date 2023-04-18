package org.liuyi.mzfreeform.page

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/4:10:16
 * @Description: 打开通知，长按快捷开关，跳转到设置，应用间跳转，分享到应用，传送门跳转
 */
@BMPage("lightOpen", "轻量打开")
class LightOpen : BasePage() {
    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV("打开通知", tips = "单击通知使用小窗打开"),
            SwitchV("open_notice")
        )

        TextSummaryWithSwitch(
            TextSummaryV("长按快捷开关", tips = "使用小窗打开控制中心的快捷方式"),
            SwitchV("long_press_tile")
        )

        TextSummaryWithSwitch(
            TextSummaryV("应用间跳转", tips = "应用间跳转时使用小窗打开"),
            SwitchV("app_jump")
        )

        TextSummaryWithSwitch(
            TextSummaryV("分享到应用", tips = "应用间分享时使用小窗打开"),
            SwitchV("share_to_app")
        )

        TextSummaryWithSwitch(
            TextSummaryV("传送门跳转", tips = "传送门跳转链接时使用小窗打开"),
            SwitchV("portal_jump")
        )

        TextSummaryWithSwitch(
            TextSummaryV("悬浮球跳转", tips = "悬浮球跳转应用时使用小窗打开"),
            SwitchV("ball_jump")
        )



    }
}