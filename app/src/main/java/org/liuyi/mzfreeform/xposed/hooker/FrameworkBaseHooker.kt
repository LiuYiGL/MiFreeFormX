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
import org.liuyi.mzfreeform.intent_extra.getFreeFormMode
import org.liuyi.mzfreeform.utils.*
import java.lang.reflect.Method

/**
 * @Author: Liuyi
 * @Date: 2023/04/22/0:16:36
 * @Description:
 */
@SuppressLint("QueryPermissionsNeeded")

object FrameworkBaseHooker : YukiBaseHooker() {

    override fun onHook() {


        val appJumpFunction: HookParam.() -> Unit? = fun HookParam.() {
            val callingPackage = args[1] as String?
            val intent = args[3] as Intent?
            val context = instance.getFieldValueOrNull("mContext") as Context?
            // 排除系统桌面
            if ("com.miui.home" == callingPackage) return
            if (intent != null && context != null) {
                if (intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0) {
                    // 包含 new 标签
                    val componentName = intent.resolveActivity(context.packageManager)
                    loggerD(msg = "$componentName")
                    componentName?.packageName?.let { name ->
                        loggerD(msg = name)
                        if (callingPackage != name) {
                            // 判断为应用间跳转
                            args[10] = args[10] ?: getBasicBundle()?.toMultiWidow(context)
                        }
                    }
                }
            }
        }

        "com.android.server.wm.ActivityTaskManagerService".hook {
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
                    if (intent != null && context != null) {
                        when (intent.getFreeFormMode()) {
                            FreeFormIntent.FREE_FORM_EXTRA_IGNORE -> return@beforeHook
                            FreeFormIntent.FREE_FORM_EXTRA_FORCE -> {
                                args[10] = args[10] ?: getBasicBundle()
                                (args[10] as? Bundle?)?.toMultiWidow(context)
                            }
                            else -> Unit
                        }
                    }
                    by(this, DataConst.APP_JUMP) {
                        appJumpFunction(this)
                    }
                }
            }
        }

        /**
         * 对 系统 启动的Activity进行 管控
         * Hook com.android.server.am.ActivityManagerService#sendIntentSender
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
                    val mContext = instance.getFieldValueOrNull("mContext") as? Context?

                    loggerD(msg = "intent: $intent and $mContext")
                    if (intent != null && mContext != null) {
                        when (intent.getFreeFormMode()) {
                            FreeFormIntent.FREE_FORM_EXTRA_IGNORE -> return@beforeHook
                            FreeFormIntent.FREE_FORM_EXTRA_FORCE -> {
                                args[7] = args[7] ?: getBasicBundle()
                                (args[7] as? Bundle?)?.toMultiWidow(mContext)
                            }
                            else -> return@beforeHook
                        }
                    }
                }
            }
        }

    }


}
