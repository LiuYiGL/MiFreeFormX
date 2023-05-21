package org.liuyi.mifreeformx.xposed.operation

import android.annotation.SuppressLint
import android.content.Intent
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.intent.LyIntent
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/7:39:12
 * @Description:
 */
object AppShareOpt {

    // 应用间分享
    internal fun isShareToApp(callingPackage: String?, intent: Intent?): Boolean {
        if (intent?.`package` == callingPackage) return false
        intent?.action.let {
            if (it == Intent.ACTION_SEND) return true
            // 使用全屏打开 miui 系统选择分享界面
            if (it == "miui.intent.action.MIUI_CHOOSER") return false
        }
        intent?.component?.let {
            // 微信分享sdk
            if (it.className == "com.tencent.mm.plugin.base.stub.WXEntryActivity") return true
            if (it.packageName == callingPackage) return false
        }
        if (intent?.clipData != null) return true
        intent?.data?.let {
            // QQ分享sdk
            if (it.scheme == "mqqapi" && it.host == "share") return true
        }
        return false
    }

    @SuppressLint("WrongConstant")
    internal fun handle(intent: Intent): Boolean {
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM)
        if (FrameworkBaseHooker.prefs.get(DataConst.SHARE_TO_APP_FORCE_NEW_TASK)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return true
    }
}