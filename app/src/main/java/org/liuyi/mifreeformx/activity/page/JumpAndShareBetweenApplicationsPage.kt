package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.R

/**
 * @Author: Liuyi
 * @Date: 2023/04/26/18:11:51
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(
    key = "JumpAndShareBetweenApplicationsPage",
    titleId = R.string.jump_and_share_between_applications
)
class JumpAndShareBetweenApplicationsPage : MyBasePage() {

    override fun onCreate() {
        TitleText(textId = R.string.jump_between_applications)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.jump_between_applications_use_window,
                tipsId = R.string.jump_between_applications_use_window_tips
            ),
            createSwitchV(DataConst.APP_JUMP)
        )
        TextSummaryWithArrow(TextSummaryV(text = "源黑名单", tips = "不使用小窗处理打开其他应用") {
            AppSelectPage.currentBlackList = BlackList.AppJumpSourceBlacklist
            showFragment("AppSelectPage")
        })
        TextSummaryWithArrow(TextSummaryV(text = "目标黑名单", tips = "对将打开的应用不处理") {
            AppSelectPage.currentBlackList = BlackList.AppJumpTargetBlacklist
            showFragment("AppSelectPage")
        })

        Line()
        TitleText(textId = R.string.share_between_applications)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.share_between_applications_use_window,
                tipsId = R.string.share_between_applications_use_window_tips
            ),
            createSwitchV(DataConst.SHARE_TO_APP)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.force_use_window_when_share,
                tipsId = R.string.force_use_window_when_share_tip
            ),
            createSwitchV(DataConst.SHARE_TO_APP_FORCE_NEW_TASK)
        )
        TextSummaryWithArrow(TextSummaryV(text = "源黑名单", tips = "不处理当前应用的分享") {
            AppSelectPage.currentBlackList = BlackList.AppShareSourceBlacklist
            showFragment("AppSelectPage")
        })
        TextSummaryWithArrow(TextSummaryV(text = "目标黑名单", tips = "不处理分享的目标应用") {
            AppSelectPage.currentBlackList = BlackList.AppShareTargetBlacklist
            showFragment("AppSelectPage")
        })
    }
}