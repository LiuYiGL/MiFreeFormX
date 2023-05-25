package org.liuyi.mifreeformx.xposed.operation

import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.param.HookParam
import org.liuyi.mifreeformx.utils.containsFlag
import org.liuyi.mifreeformx.utils.logD

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/7:36:24
 * @Description:
 */
object AppJumpOpt {

    // 应用间跳转
    fun HookParam.isAppJump(callingThread: Any?, callingPackage: String?, intent: Intent, context: Context): Boolean {
        callingPackage ?: return false
        // 排除如，系统后台进入，获取桌面进入
        if (intent.flags and Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED != 0) return false
        // 排除小米妙享的文件管理和笔记
        if (intent.action == "miui.intent.action.OUTBOUND_APP") return false

        val targetActivity = intent.component ?: intent.resolveActivity(context.packageManager)
        logD("isAppJump: Target: $targetActivity")
        val isSameApp = callingPackage == targetActivity.packageName
        if (isSameApp) return false

        // 浏览器相关
        if (intent.action == Intent.ACTION_VIEW && intent.hasCategory(Intent.CATEGORY_BROWSABLE)) {
            return true
        }

        // 修复微信分享回调导致源activity使用小窗
        if (intent.containsFlag(Intent.FLAG_ACTIVITY_NEW_TASK)) {
            // 修复微信分享回调导致源activity使用小窗
            if (targetActivity.className.endsWith(".wxapi.WXEntryActivity")) return false
            return true
        }
        return false
    }
}