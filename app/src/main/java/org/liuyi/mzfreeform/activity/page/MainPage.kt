package org.liuyi.mzfreeform.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.R

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:32:24
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMMainPage(titleId = R.string.app_name)
class MainPage : MyBasePage() {
    override fun onCreate() {

        TextWithSwitch(
            TextV(textId = R.string.main_switch),
            createSwitchV(DataConst.MAIN_SWITCH)
        )
        Line()
        TitleText(textId = R.string.function)
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.notification_and_controlcenter) {
                showFragment("NotificationAndControlCenterPage")
            }
        )
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.jump_and_share_between_applications) {
                showFragment("JumpAndShareBetweenApplicationsPage")
            }
        )
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.parallel_small_window) {
                showFragment("ParallelSmallWindowPage")
            }
        )
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.miscellaneous) {
                showFragment("MiscellaneousPage")
            }
        )
    }
}

