package org.liuyi.mifreeformx.xposed.operation

import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.log.loggerD

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/7:36:24
 * @Description:
 */
object AppJumpOpt {

    // 应用间跳转
    fun isAppJump(callingThread: Any?, callingPackage: String?, intent: Intent, context: Context): Boolean {
        // 排除如，系统后台进入，获取桌面进入
        if (intent.flags and Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED != 0) return false
        if (callingPackage == "com.miui.securityadd") return false
        if (intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0) {
            // 包含 new 标签
            val componentName = intent.component ?: intent.resolveActivity(context.packageManager)
            loggerD(msg = "$componentName")
            // 修复微信分享回调导致源activity使用小窗
            if (componentName.className.endsWith(".wxapi.WXEntryActivity")) return false
            componentName?.packageName?.let { name ->
                loggerD(msg = name)
                if (callingPackage != name) {
                    // 判断为应用间跳转
                    return true
                }
            }
        }
        return false
    }
}