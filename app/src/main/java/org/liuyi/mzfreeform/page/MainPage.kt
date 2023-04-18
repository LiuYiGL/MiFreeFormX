package org.liuyi.mzfreeform.page

import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.activity.view.TextWithSwitchV

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:32:24
 * @Description:
 */
@BMMainPage("魅窗")
class MainPage : BasePage() {
    override fun onCreate() {

        TextWithSwitch(TextV("总开关"), SwitchV("main_switch", true))
        Line()
        TextSummaryWithArrow(
            TextSummaryV("轻量打开", tips = "管理使用小窗打开场景") {
                showFragment("lightOpen")
            }
        )


    }
}