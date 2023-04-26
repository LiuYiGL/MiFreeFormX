package org.liuyi.mzfreeform.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.R

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
    }
}