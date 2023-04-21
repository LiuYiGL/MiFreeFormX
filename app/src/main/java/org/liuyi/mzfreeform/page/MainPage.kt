package org.liuyi.mzfreeform.page

import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.activity.view.TextWithSwitchV
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.xposed.channel.YukiHookDataChannel
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mzfreeform.DataConst

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:32:24
 * @Description:
 */
@BMMainPage("魅窗")
class MainPage : BasePage() {
    override fun onCreate() {

        TextWithSwitch(
            TextV("总开关"),
            createSwitchV(DataConst.MAIN_SWITCH)
        )
        Line()
        TextSummaryWithArrow(
            TextSummaryV("轻量打开", tips = "管理使用小窗打开场景") {
                showFragment("lightOpen")
            }
        )

        Line()
        TitleText(text = "通知和控制中心")
        TextSummaryWithSwitch(
            TextSummaryV("打开通知", tips = "单击通知使用小窗打开"),
            createSwitchV(DataConst.OPEN_NOTICE)
        )
        TextSummaryWithSwitch(
            TextSummaryV("长按快捷开关", tips = "使用小窗打开控制中心的快捷方式，默认强制禁用小窗黑名单"),
            createSwitchV(DataConst.LONG_PRESS_TILE)
        )
        TextSummaryWithSwitch(
            TextSummaryV("强制所有小窗打开", tips = "在上面的基础上，尽量全部使用小窗打开，哪怕不符合操作逻辑"),
            createSwitchV(DataConst.FORCE_CONTROL_ALL_OPEN)
        )

        Line()
        TitleText(text = "增强")
        TextWithSwitch(
            TextV("禁用小窗黑名单"),
            createSwitchV(DataConst.DISABLE_FREEFORM_BLACKLIST)
        )
//        TextWithSwitch(
//            TextV("强制所有活动可调整大小"),
//            createSwitchV(DataConst.FORCE_ACTIVITY_RESIZEABLE)
//        )
    }

    private fun createSwitchV(prefsData: PrefsData<Boolean>) =
        SwitchV(prefsData.key, activity.prefs().get(prefsData)) {
            activity.prefs().edit { put(prefsData, it) }
        }
}

