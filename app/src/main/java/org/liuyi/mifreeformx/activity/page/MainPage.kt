package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import com.highcapable.yukihookapi.hook.log.loggerE
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.BuildConfig
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.utils.startUriString


/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:32:24
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMMainPage(titleId = R.string.app_name)
class MainPage : MyBasePage() {

    companion object {
        val wechat_admiration_intent = Intent().apply {
            component = ComponentName(
                "com.tencent.mm",
                "com.tencent.mm.plugin.collect.reward.ui.QrRewardSelectMoneyUI"
            )
            putExtra("key_qrcode_url", "m0j\$Uk*ue+eMd)Uxm1@@RS")
        }
        val alipay_donate_intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("alipays://platformapi/startapp?appId=20000067&url=https://qr.alipay.com/fkx17518ynrnmhygunamf10")
        )
    }

    override fun onCreate() {

        TextWithSwitch(
            TextV(textId = R.string.main_switch), createSwitchV(DataConst.MAIN_SWITCH)
        )
        Line()
        TitleText(textId = R.string.function)
        TextSummaryWithArrow(TextSummaryV(textId = R.string.notification_and_controlcenter) {
            showFragment("NotificationAndControlCenterPage")
        })
        TextSummaryWithArrow(TextSummaryV(textId = R.string.jump_and_share_between_applications) {
            showFragment("JumpAndShareBetweenApplicationsPage")
        })
        TextSummaryWithArrow(TextSummaryV(textId = R.string.parallel_small_window) {
            showFragment("ParallelSmallWindowPage")
        })
        TextSummaryWithArrow(TextSummaryV(textId = R.string.miscellaneous) {
            showFragment("MiscellaneousPage")
        })
        Line()
        TitleText(textId = R.string.module_settings)
        TextWithSwitch(TextV("隐藏桌面图标"), SwitchV("hLauncherIcon") {
            activity.packageManager.setComponentEnabledSetting(
                ComponentName(activity, "${BuildConfig.APPLICATION_ID}.launcher"),
                if (it) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        })

        Line()
        TitleText(textId = R.string.discussion)
        TextSummaryWithArrow(TextSummaryV(textId = R.string.group_qq) {
            activity.startUriString("https://qm.qq.com/cgi-bin/qm/qr?k=vFmdInhwejDS4ra29weLXlNHVE-CPoMA")
        })

        Line()
        TitleText(textId = R.string.other)
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.wechat_admiration) {
                kotlin.runCatching {
                    activity.startActivity(wechat_admiration_intent)
                }.exceptionOrNull()?.let {
                    loggerE(e = it)
                    showFragment("DonatePage")
                }
            }
        )
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.alipay_donation) {
                kotlin.runCatching {
                    activity.startActivity(alipay_donate_intent)
                }.exceptionOrNull()?.let {
                    loggerE(e = it)
                    Toast.makeText(activity, "请检查支付宝是否是最新版本", Toast.LENGTH_SHORT).show()
                    showFragment("DonatePage")
                }
            }
        )
        TextSummaryWithArrow(
            TextSummaryV(textId = R.string.donation_code, tipsId = R.string.donation_tips) {
                showFragment("DonatePage")
            }
        )
        TextSummaryWithArrow(TextSummaryV(textId = R.string.about) {
            showFragment("AboutPage")
        })

    }
}

