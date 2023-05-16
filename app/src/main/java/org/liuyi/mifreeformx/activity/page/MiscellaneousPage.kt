package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.xposed.hooker.FreeformOutsideMotionHooker

/**
 * @Author: Liuyi
 * @Date: 2023/04/26/19:23:42
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(key = "MiscellaneousPage", titleId = R.string.miscellaneous)
class MiscellaneousPage : MyBasePage() {
    override fun onCreate() {
        TextWithSwitch(
            TextV(textId = R.string.disable_window_blacklist),
            createSwitchV(DataConst.DISABLE_FREEFORM_BLACKLIST)
        )
        TextWithSwitch(
            TextV(textId = R.string.lift_window_num_limit),
            createSwitchV(DataConst.LIFT_WINDOW_NUM_LIMIT)
        )
        TextSummaryWithSpinner(
            TextSummaryV(
                textId = R.string.click_freeform_outside_action,
                tipsId = R.string.click_freeform_outside_action_tips
            ),
            createSpinnerV(
                FreeformOutsideMotionHooker.CLICK_FREEFORM_OUTSIDE_ACTION_TYPE,
                FreeformOutsideMotionHooker.CLICK_FREEFORM_OUTSIDE_ACTION_TYPE_STRING
            )
        )
    }
}