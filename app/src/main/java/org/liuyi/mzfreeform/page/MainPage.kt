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
            TextV("模块总开关"),
            createSwitchV(DataConst.MAIN_SWITCH)
        )
        Line()
        TitleText(text = "功能")
        TextSummaryWithArrow(
            TextSummaryV("通知和控制中心") {
                showFragment("NotificationAndControlCenterPage")
            }
        )


        TextSummaryWithSwitch(
            TextSummaryV("应用间跳转", tips = "应用间跳转时使用小窗打开"),
            createSwitchV(DataConst.APP_JUMP)
        )

        TextSummaryWithArrow(
            TextSummaryV("通知和控制中心") {
                showFragment("ShareToAppPage")
            }
        )

        Line()
        TitleText(text = "特色功能")
        TextSummaryWithSwitch(
            TextSummaryV("平行小窗Plus", tips = "实现边刷文章边回消息，入口是通知，侧边栏，悬浮球"),
            createSwitchV(DataConst.PARALLEL_MULTI_WINDOW_PLUS)
        )


        Line()
        TitleText(text = "增强")
        TextWithSwitch(
            TextV("禁用小窗黑名单"),
            createSwitchV(DataConst.DISABLE_FREEFORM_BLACKLIST)
        )
        TextWithSwitch(
            TextV("禁用多小窗间的位置偏移"),
            createSwitchV(DataConst.DISABLE_MULTI_OFFSET)
        )
        TextWithSwitch(
            TextV("解除小窗数量限制"),
            createSwitchV(DataConst.CANCEL_MULTI_WINDOW_LIMIT)
        )
        TextSummaryWithSwitch(
            TextSummaryV("禁用启动应用确认时缩放当前应用", tips = "修复打开小窗应用触发二次确认时会缩小当前应用的Bug"),
            createSwitchV(DataConst.FIX_START_SMALL_WINDOW_CONFIRM)
        )
    }

    private fun createSwitchV(prefsData: PrefsData<Boolean>) =
        SwitchV(prefsData.key, activity.prefs().get(prefsData)) {
            activity.prefs().edit { put(prefsData, it) }
        }
}

