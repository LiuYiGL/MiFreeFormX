package org.liuyi.mifreeformx.xposed.hooker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.intent_extra.FreeFormIntent
import org.liuyi.mifreeformx.intent_extra.forceFreeFromMode
import org.liuyi.mifreeformx.intent_extra.getFreeFormMode
import org.liuyi.mifreeformx.intent_extra.setFreeFromBundle
import org.liuyi.mifreeformx.utils.*

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
                    val caller = args[1] as String?
                    val intent = Intent(args[3] as? Intent?)
                    args[3] = intent
                    val context = instance.getFieldValueOrNull("mContext") as Context?
                    if (isInBlacklist(context, intent)) return@beforeHook

                    if (context != null) {
                        by(this, DataConst.PARALLEL_MULTI_WINDOW_PLUS) {
                            if (caller == "com.miui.touchassistant" || caller == "com.miui.securitycenter") {
                                if (!intent.isSameApp(caller) && intent.action == Intent.ACTION_MAIN) {
                                    intent.forceFreeFromMode()
                                    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                                }
                            }
                        }
                        by(this, DataConst.APP_JUMP) {
                            if (isAppJump(args[0], caller, intent, context)) {
                                intent.forceFreeFromMode()
                            }
                        }
                        by(this, DataConst.SHARE_TO_APP) {
                            // 开启了分享至应用
                            if (isShareToApp(args[1] as? String?, intent)) {
                                intent.forceFreeFromMode()
                                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                                // 强制添加 new task 标签
                                by(this, DataConst.SHARE_TO_APP_FORCE_NEW_TASK) {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
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
                        val intent = Intent(args[2] as? Intent?)
                        args[2] = intent
                        if (isInBlacklist(context, intent)) return@by
                        if (context != null) {
                            if (isShareToApp(args[1] as? String?, intent)) {
                                // 开启分享应用
                                intent.forceFreeFromMode()
                                // 强制添加 new task 标签
                                by(this, DataConst.SHARE_TO_APP_FORCE_NEW_TASK) {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
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
         * 对 系统 启动的Activity进行 管控，系统PendingIntent会send到这里，通常都是通知
         * "com.android.server.wm.ActivityTaskManagerService\$LocalService#startActivityInPackage"
         */
        "com.android.server.wm.ActivityTaskManagerService\$LocalService".hook {
            injectMember {
                method { name("startActivityInPackage") }
                beforeHook {
                    loggerD(msg = "${args.asList()}")
                    by(this, DataConst.PARALLEL_MULTI_WINDOW_PLUS) {
                        // 排除一些系统进程
                        if (args[0] == 1000) return@by
                        val intent = Intent(args[5] as? Intent?)
                        args[5] = intent
                        intent.removeFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    }
                }
            }
        }

    }


}
