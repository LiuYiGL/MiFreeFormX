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
            TextSummaryV("通知和控制中心") {
                showFragment("NotificationAndControlCenterPage")
            }
        )


        Line()
        TitleText(text = "增强")
        TextWithSwitch(
            TextV("禁用小窗黑名单"),
            createSwitchV(DataConst.DISABLE_FREEFORM_BLACKLIST)
        )
    }

    private fun createSwitchV(prefsData: PrefsData<Boolean>) =
        SwitchV(prefsData.key, activity.prefs().get(prefsData)) {
            activity.prefs().edit { put(prefsData, it) }
        }
}

