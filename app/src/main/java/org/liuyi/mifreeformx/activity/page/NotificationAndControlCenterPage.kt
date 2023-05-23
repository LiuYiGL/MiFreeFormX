package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import android.view.View
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import com.highcapable.yukihookapi.hook.factory.prefs
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.xposed.hooker.systemui.ClickNotificationHooker
import org.liuyi.mifreeformx.xposed.hooker.systemui.LongClickTileHooker

/**
 * @Author: Liuyi
 * @Date: 2023/04/21/19:45:22
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage("NotificationAndControlCenterPage", titleId = R.string.notification_and_controlcenter)
class NotificationAndControlCenterPage : MyBasePage() {


    private val clickNotificationViewBinding =
        GetDataBinding({
            val mode = activity.prefs().get(ClickNotificationHooker.OPEN_MODE)
            ClickNotificationHooker.OPEN_MODE_TEXT[mode]
        }) { view, i, any ->
            when (any) {
                ClickNotificationHooker.OPEN_MODE_TEXT[0] -> view.visibility = View.GONE
                ClickNotificationHooker.OPEN_MODE_TEXT[1] -> when (i) {
                    1 -> view.visibility = View.GONE
                    2 -> view.visibility = View.VISIBLE
                }

                else -> view.visibility = View.VISIBLE
            }
        }

    private val longClickTileViewBinding =
        GetDataBinding({
            val mode = activity.prefs().get(LongClickTileHooker.OPEN_MODE)
            LongClickTileHooker.OPEN_MODE_TEXT[mode]
        }) { view, _, any ->
            LongClickTileHooker.run {
                when (any) {
                    OPEN_MODE_TEXT[0], OPEN_MODE_TEXT[2] -> view.visibility = View.GONE
                    OPEN_MODE_TEXT[1] -> view.visibility = View.VISIBLE
                }
            }
        }

    override fun onCreate() {
        TitleText(textId = R.string.notification)
        TextSummaryWithSpinner(
            TextSummaryV(
                textId = R.string.click_notice_open_widow,
                tipsId = R.string.click_notice_open_widow_tips
            ),
            createSpinnerV(
                ClickNotificationHooker.OPEN_MODE,
                ClickNotificationHooker.OPEN_MODE_TEXT,
                clickNotificationViewBinding.bindingSend
            )
        )
        TextSA("选择应用", dataBindingRecv = clickNotificationViewBinding.getRecv(1), onClickListener = {
            AppSelectPage.preList = ClickNotificationHooker.SELECTED_APPS_LIST
            showFragment("AppSelectPage")
        })
        TextSummaryWithSwitch(
            TextSummaryV("忽略锁屏", tips = "处于锁屏状态时不使用小窗打开"),
            createSwitchV(ClickNotificationHooker.OPEN_NOTICE_SKIP_LOCKSCREEN),
            clickNotificationViewBinding.getRecv(2)
        )
        TextSA("忽略当前应用", tips = "处于当前应用时不使用小窗打开",
            dataBindingRecv = clickNotificationViewBinding.getRecv(1),
            onClickListener = {
                AppSelectPage.preList = ClickNotificationHooker.IGNORE_TOP_SELECTED_APPS_LIST
                showFragment("AppSelectPage")
            }
        )
        TitleText(text = "增强")
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_window_notice_limit,
                tipsId = R.string.remove_window_notice_limit_tips
            ),
            createSwitchV(DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW)
        )

        Line()
        TitleText(textId = R.string.control_center)
        TextSummaryWithSpinner(
            TextSummaryV(
                textId = R.string.long_press_tile_open_window,
                tipsId = R.string.long_press_tile_open_window_tips
            ),
            createSpinnerV(
                LongClickTileHooker.OPEN_MODE,
                LongClickTileHooker.OPEN_MODE_TEXT,
                longClickTileViewBinding.bindingSend
            )
        )
        TextSummaryWithArrow(TextSummaryV(text = "黑名单") {
            AppSelectPage.preList = LongClickTileHooker.BlackList
            showFragment("AppSelectPage")
        }, longClickTileViewBinding.getRecv(0))

    }
}