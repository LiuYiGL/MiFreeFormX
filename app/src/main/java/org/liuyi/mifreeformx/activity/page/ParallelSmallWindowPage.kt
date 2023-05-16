package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.R

/**
 * @Author: Liuyi
 * @Date: 2023/04/26/19:10:58
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(key = "ParallelSmallWindowPage", titleId = R.string.parallel_small_window)
class ParallelSmallWindowPage : MyBasePage() {
    override fun onCreate() {

        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.open_parallel_small_window,
                tipsId = R.string.open_parallel_small_window_tips
            ),
            createSwitchV(DataConst.PARALLEL_MULTI_WINDOW_PLUS)
        )
        TextSummaryWithArrow(TextSummaryV(text = "选择应用") {
            AppSelectPage.currentBlackList = BlackList.ParallelFreeformWhitelist
            showFragment("AppSelectPage")
        })
    }
}