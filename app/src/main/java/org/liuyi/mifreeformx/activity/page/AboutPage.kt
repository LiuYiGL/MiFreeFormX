package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.BuildConfig
import org.liuyi.mifreeformx.utils.startUriString

/**
 * @Author: Liuyi
 * @Date: 2023/04/28/13:25:54
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(key = "AboutPage", titleId = R.string.about)
class AboutPage : MyBasePage() {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate() {
        ImageWithText(
            activity.getDrawable(R.drawable.icon)!!,
            activity.getString(R.string.app_name),
            "版本：${BuildConfig.VERSION_NAME}"
        )

        Line()
        TitleText(textId = R.string.developer)
        TextSummaryWithArrow(TextSummaryV(textId = R.string.coolapk_author) {
            activity.startUriString("coolmarket://u/1735098")
        })

        Line()
        TitleText(textId = R.string.thanks)
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.yuki_hook_api_at_authod,
                tipsId = R.string.yuki_hook_api_tips
            ) {
                activity.startUriString("https://github.com/fankes/YukiHookAPI")
            })
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.blockmiui_at_authod,
                tipsId = R.string.blockmiui_tips
            ) {
                activity.startUriString("https://github.com/Block-Network/blockmiui")
            })
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.android_util_code_at_authod,
                tipsId = R.string.android_util_code_tips
            ) {
                activity.startUriString("https://github.com/Blankj/AndroidUtilCode")
            })
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.max_free_form_at_authod,
                tipsId = R.string.max_free_form_tips
            ) {
                activity.startUriString("https://github.com/YifePlayte/MaxFreeForm")
            })
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.woo_box_for_miui_at_authod,
                tipsId = R.string.woo_box_for_miui_tips
            ) {
                activity.startUriString("https://github.com/MoralNorm/WooBoxForMIUI")
            })


        Line()
        TitleText(textId = R.string.other)
        TextSummaryWithArrow(TextSummaryV(textId = R.string.open_source) {
            activity.startUriString("https://github.com/LiuYiGL/MiFreeFormX")
        })
        TextSummaryWithArrow(TextSummaryV(textId = R.string.feedback) {
            activity.startUriString("https://github.com/LiuYiGL/MiFreeFormX/issues")
        })
    }
}