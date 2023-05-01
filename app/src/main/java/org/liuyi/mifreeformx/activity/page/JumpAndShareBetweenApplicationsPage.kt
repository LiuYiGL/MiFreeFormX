package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
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
    }
}