package org.liuyi.mzfreeform.xposed.hooker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.param.HookParam
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.intent_extra.FreeFormIntent
import org.liuyi.mzfreeform.intent_extra.forceFreeFromMode
import org.liuyi.mzfreeform.intent_extra.getFreeFormMode
import org.liuyi.mzfreeform.intent_extra.setFreeFromBundle
import org.liuyi.mzfreeform.utils.*

/**
 * @Author: Liuyi
 * @Date: 2023/04/22/0:16:36
 * @Description:
 */
@SuppressLint("QueryPermissionsNeeded")

object FrameworkBaseHooker : YukiBaseHooker() {

    private val targetBlacklist =
        listOf("com.android.camera", "com.miui.tsmclient", "com.lbe.security.miui")

    // 禁止小窗黑名单
    private fun isInBlacklist(context: Context? = null, intent: Intent?): Boolean {
        intent ?: return false
        intent.component?.let {
            if (targetBlacklist.contains(it.packageName)) return true
        }
        intent.`package`?.let {
            if (targetBlacklist.contains(it)) return true
        }
        context ?: return false
        context.packageManager?.let {
            val componentName = intent.resolveActivity(it)
            componentName?.run {
                if (targetBlacklist.contains(packageName)) return true
            }
        }
        return false
    }

    // 应用间跳转
    private fun isAppJump(
        callingThread: Any?, callingPackage: String?, intent: Intent, context: Context
    ): Boolean {
        // 排除系统调用
        callingThread ?: return false
        // 排除如，系统后台进入，获取桌面进入
        if (intent.flags and Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED != 0) return false
        if (callingPackage == "com.miui.securityadd") return false
        if (intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0) {
            // 包含 new 标签
            val componentName = intent.resolveActivity(context.packageManager)
            loggerD(msg = "$componentName")
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

    // 应用间分享
    private fun isShareToApp(callingPackage: String?, intent: Intent?): Boolean {
        val res: Boolean = when {
            intent?.`package` == callingPackage -> false
            intent?.action == Intent.ACTION_SEND -> true
            intent?.component != null ->
                intent.component?.let {
                    when (it.packageName) {
                        callingPackage -> false
                        "com.miui.packageinstaller" -> it.className == "com.miui.packageInstaller.NewPackageInstallerActivity"
                        "com.tencent.mm" -> it.className.contains(".plugin.base.stub.WXEntryActivity")
                        else -> false
                    }
                } ?: false
            intent?.clipData != null -> true
            intent?.data != null -> true
            else -> false
        }
        // 强制添加 new task 标签
        if (res && prefs.direct().get(DataConst.SHARE_TO_APP_FORCE_NEW_TASK)) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return res
    }


    override fun onHook() {

        "com.android.server.wm.ActivityTaskManagerService".hook {
            /**
             * 用户应用调用会来到这里
             */
            injectMember {
                method {
                    name("startActivityAsUser")
                    paramCount(13)
                }
                beforeHook {
                    loggerD(msg = "${this.args.asList()}")
                    // 全局管控，只要在intent设置了 FreeFormIntent 都会优先判断是否开启小窗
                    val intent = args[3] as Intent?
                    val context = instance.getFieldValueOrNull("mContext") as Context?
                    if (isInBlacklist(context, intent)) return@beforeHook

                    if (intent != null && context != null) {
                        by(this, DataConst.APP_JUMP) {
                            if (isAppJump(args[0], args[1] as? String?, intent, context)) {
                                intent.forceFreeFromMode()
                            }
                        }
                        by(this, DataConst.SHARE_TO_APP) {
                            // 开启了分享至应用
                            if (isShareToApp(args[1] as? String?, intent)) {
                                intent.forceFreeFromMode()
                            }
                        }
                        if (intent.getFreeFormMode() == FreeFormIntent.FREE_FORM_EXTRA_FORCE) {
                            args[10] = args[10] ?: getBasicBundle()
                            intent.setFreeFromBundle(args[10] as Bundle, context)
                        }
                    }
                }
            }
            /**
             * 系统分享时会来到这里
             */
            injectMember {
                method { name("startActivityAsCaller") }
                beforeHook {
                    by(this, DataConst.SHARE_TO_APP) {
                        val context = instance.getFieldValueOrNull("mContext") as? Context?
                        val intent = args[2] as? Intent?
                        if (isInBlacklist(context, intent)) return@by
                        if (context != null && intent != null) {
                            if (isShareToApp(args[1] as? String?, intent)) {
                                // 开启分享应用
                                intent.forceFreeFromMode()
                            }
                            if (intent.getFreeFormMode() == FreeFormIntent.FREE_FORM_EXTRA_FORCE) {
                                args[9] = args[9] ?: getBasicBundle()
                                intent.setFreeFromBundle(args[9] as Bundle, context)
                            }
                        }
                    }
                }
            }
        }


        /**
         * 对 系统 启动的Activity进行 管控
         * Hook com.android.server.am.ActivityManagerService#sendIntentSender
         * 通常是一些系统应用如点击通知栏等进入这里
         */
        "com.android.server.am.ActivityManagerService".hook {
            injectMember {
                method {
                    name("sendIntentSender")
                }
                beforeHook {
                    loggerD(msg = "${this.args.asList()}")
                    // 全局管控，只要在intent设置了 FreeFormIntent 都会优先判断是否开启小窗
                    val intent = args[3] as Intent?
                    val context = instance.getFieldValueOrNull("mContext") as? Context?
                    if (isInBlacklist(context, intent)) return@beforeHook

                    loggerD(msg = "intent: $intent and $context")
                    if (intent != null && context != null) {
                        if (intent.getFreeFormMode() == FreeFormIntent.FREE_FORM_EXTRA_FORCE) {
                            args[7] = args[7] ?: getBasicBundle()
                            intent.setFreeFromBundle(args[7] as Bundle, context)
                        }
                    }
                }
            }
        }

    }


}
