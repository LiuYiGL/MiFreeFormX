package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import android.view.View
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import com.highcapable.yukihookapi.hook.factory.prefs
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.xposed.hooker.FreeformOutsideMotionHooker
import org.liuyi.mifreeformx.xposed.hooker.SizeAndPositionHooker

/**
 * @Author: Liuyi
 * @Date: 2023/04/26/19:23:42
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(key = "MiscellaneousPage", titleId = R.string.miscellaneous)
class MiscellaneousPage : MyBasePage() {

    private val freeformOutsideViewBinding = GetDataBinding({
        val index = activity.prefs().get(FreeformOutsideMotionHooker.FREEFORM_OUTSIDE_MOTION_MODE)
        FreeformOutsideMotionHooker.FREEFORM_OUTSIDE_MOTION_MODE_STRING[index]
    }) { view, _, data ->
        when (data) {
            FreeformOutsideMotionHooker.FREEFORM_OUTSIDE_MOTION_MODE_STRING[0] -> view.visibility = View.GONE
            else -> view.visibility = View.VISIBLE
        }
    }

    override fun onCreate() {
        TextWithSwitch(
            TextV(textId = R.string.disable_window_blacklist),
            createSwitchV(DataConst.DISABLE_FREEFORM_BLACKLIST)
        )
        TextWithSwitch(
            TextV(textId = R.string.lift_window_num_limit),
            createSwitchV(DataConst.LIFT_WINDOW_NUM_LIMIT)
        )
        Line()
        TitleText("小窗以外区域手势")
        TextSummaryWithSpinner(
            TextSummaryV(
                textId = R.string.freeform_outside_motion_mode,
            ),
            createSpinnerV(
                FreeformOutsideMotionHooker.FREEFORM_OUTSIDE_MOTION_MODE,
                FreeformOutsideMotionHooker.FREEFORM_OUTSIDE_MOTION_MODE_STRING,
                freeformOutsideViewBinding.bindingSend
            ),
        )
        TextSummaryWithSpinner(
            TextSummaryV(
                textId = R.string.freeform_outside_motion_action_mode
            ),
            createSpinnerV(
                FreeformOutsideMotionHooker.CLICK_FREEFORM_OUTSIDE_ACTION_TYPE,
                FreeformOutsideMotionHooker.CLICK_FREEFORM_OUTSIDE_ACTION_TYPE_STRING
            ),
            freeformOutsideViewBinding.getRecv(0)
        )
    }
}