package org.liuyi.mzfreeform.xposed.hooker

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.android.ActivityInfoClass
import com.highcapable.yukihookapi.hook.type.android.ActivityManagerClass
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.intent_extra.FreeFormIntent
import org.liuyi.mzfreeform.intent_extra.forceFreeFromMode
import org.liuyi.mzfreeform.utils.*
import kotlin.math.log

/**
 * @Author: Liuyi
 * @Date: 2023/04/19/23:27:09
 * @Description: 可用作状态栏上的各种小窗打开，点击通知、长按快捷方式
 */
object SystemUiHooker : YukiBaseHooker() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onHook() {
        var context: Context? = null

        val CommonUtilClass = "com.miui.systemui.util.CommonUtil".toClass()
        val getTopActivity: () -> ComponentName? = {
            CommonUtilClass.method { name("getTopActivity") }.get().invoke<ComponentName>()
        }

        /**
         * Hook com.android.systemui.CoreStartable 构造器获取 Context
         */
        "com.android.systemui.CoreStartable".hook {
            injectMember(tag = "com.android.systemui.CoreStartable#CoreStartable") {
                constructor { paramCount(1) }
                beforeHook {
                    context = if (args[0] is Context) args[0] as Context? else null
                    if (context != null) removeSelf()
                }
            }
        }
        /**
         * Hook com.android.systemui.statusbar.phone.CentralSurfacesImpl#startActivityDismissingKeyguard
         * (android.content.Intent, boolean, boolean, boolean, com.android.systemui.plugins.ActivityStarter.Callback,
         * int, com.android.systemui.animation.ActivityLaunchAnimator.Controller, android.os.UserHandle)
         *
         * 当 长按 Tile 时触发方法，可在此进行判断
         */
        "com.android.systemui.statusbar.phone.CentralSurfacesImpl".hook {
            injectMember(tag = "CentralSurfacesImpl#startActivityDismissingKeyguard") {
                method {
                    name("startActivityDismissingKeyguard")
                    paramCount(8)
                }
                beforeHook {
                    by(this, DataConst.LONG_PRESS_TILE) {
                        (args[0] as Intent?)?.apply {
                            loggerD(msg = "intent: $this")

                            kotlin.runCatching {
                                val topActivity = getTopActivity()
                                val componentName: ComponentName? =
                                    resolveActivity(context!!.packageManager)
                                loggerD(msg = "$topActivity and $componentName")
                                requireNotNull(topActivity)
                                requireNotNull(componentName)
                                if (componentName.packageName == topActivity.packageName) {
                                    return@by
                                }
                            }.exceptionOrNull()?.let { loggerE(e = it) }

                            if (prefs.direct().get(DataConst.FORCE_CONTROL_ALL_OPEN)) {
                                forceFreeFromMode()
                                return@by
                            }
                            customIntent(this)
                        }
                    }
                }
            }
        }

        /**
         * Hook com.android.systemui.statusbar.phone.MiuiStatusBarNotificationActivityStarter#startNotificationIntent
         * 当点击通知时会触发这个方法，可从第一个参数 获取PendingIntent 然后反射getIntent 获得Intent
         */
        "com.android.systemui.statusbar.phone.MiuiStatusBarNotificationActivityStarter".hook {
            injectMember {
                method { name("startNotificationIntent") }
                beforeHook {
                    loggerD(msg = "${this.args.asList()}")
                    by(this, DataConst.OPEN_NOTICE) {
                        args[1] = args[1] ?: Intent()
                        (args[1] as Intent?)?.forceFreeFromMode()
                    }
                }
            }
        }


        /**
         * 解除小窗展开通知限制
         */
        "com.android.systemui.statusbar.notification.NotificationSettingsManager".hook {
            injectMember {
                method { name("canSlide") }
                beforeHook {
                    by(this, DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW) {
                        resultTrue()
                    }
                }
            }
        }

        /**
         * 解除小窗展开通知限制
         * Hook com.android.systemui.statusbar.notification.policy.AppMiniWindowManager#canNotificationSlide
         */
        "com.android.systemui.statusbar.notification.policy.AppMiniWindowManager".hook {
            injectMember {
                method { name("canNotificationSlide") }
                beforeHook {
                    by(this, DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW) {
                        resultTrue()
                    }
                }
            }
        }

    }

    /**
     * 通过Intent 判断是否需要使用小窗打开
     *
     * @param context
     * @param intent
     * @return
     */
    private fun customIntent(intent: Intent?) {
        intent?.apply {
            if (prefs.direct().get(DataConst.FORCE_CONTROL_ALL_OPEN)) {
                forceFreeFromMode()
                return
            }
            action?.let {
                when {
                    it == "android.service.quicksettings.action.QS_TILE_PREFERENCES" -> forceFreeFromMode()
                    it.startsWith("android.settings") && it != "android.settings.SETTINGS" -> forceFreeFromMode()
                }
                return
            }
            component?.let {
                when (it.packageName) {
                    "com.android.settings", "com.android.phone" -> forceFreeFromMode()
                }
                return
            }
            `package`?.let {
                if (it.startsWith("com.android")) forceFreeFromMode()
            }
        }
    }
}
