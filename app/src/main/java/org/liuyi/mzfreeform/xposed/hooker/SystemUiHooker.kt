package org.liuyi.mzfreeform.xposed.hooker

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.android.ActivityInfoClass
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.utils.ActivityOptionsUtils
import org.liuyi.mzfreeform.utils.MiuiMultiWindowUtils
import org.liuyi.mzfreeform.utils.setLaunchWindowingModeExt

/**
 * @Author: Liuyi
 * @Date: 2023/04/19/23:27:09
 * @Description: 可用作状态栏上的各种小窗打开，点击通知、长按快捷方式
 */
object SystemUiHooker : YukiBaseHooker() {

    override fun onHook() {
        var context: Context? = null

        var nextIsMod = false

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
         * Hook com.android.systemui.statusbar.phone.CentralSurfaces#getActivityOptions(int, RemoteAnimationAdapter)
         *
         * 替换Bundle
         */
        "com.android.systemui.statusbar.phone.CentralSurfaces".hook {
            injectMember(tag = "CentralSurfaces#getActivityOptions(int, RemoteAnimationAdapter)") {
                method {
                    name("getActivityOptions")
                    paramCount(2)
                }
                afterHook {
                    if (!nextIsMod) return@afterHook
                    nextIsMod = false
                    context?.let {
                        runCatching {
                            val rect = MiuiMultiWindowUtils.getFreeformRect(it)
                            val activityOptions = ActivityOptionsUtils.constructor(result as Bundle)
                            activityOptions.launchBounds = rect
                            activityOptions.setLaunchWindowingModeExt(5)
                            result = activityOptions.toBundle()
                        }.exceptionOrNull()?.let {
                            loggerE(e = it)
                        }
                    }
                }
            }
        }.by { prefs.get(DataConst.LONG_PRESS_TILE) || prefs.get(DataConst.OPEN_NOTICE) }

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
                    (args[0] as Intent).apply {
                        nextIsMod = nextIsModByIntent(context, this)
                    }
                }
            }
        }.by { prefs.get(DataConst.LONG_PRESS_TILE) }

        /**
         * Hook com.android.systemui.statusbar.phone.MiuiStatusBarNotificationActivityStarter#startNotificationIntent
         * 当点击通知时会触发这个方法，可从第一个参数 获取PendingIntent 然后反射getIntent 获得Intent
         */
        "com.android.systemui.statusbar.phone.MiuiStatusBarNotificationActivityStarter".hook {
            injectMember {
                method { name("startNotificationIntent") }
                beforeHook {
                    nextIsMod = true
                }
            }
        }.by { prefs.get(DataConst.OPEN_NOTICE) }

    }

    private fun nextIsModByIntent(context: Context? = null, intent: Intent?): Boolean {
        var isMod = false
        if (prefs.get(DataConst.FORCE_CONTROL_ALL_OPEN)) {
            return true
        }
        intent?.run {
            loggerD(msg = "intent: $intent")
//            context?.packageManager?.let {
//                val activities = it.queryIntentActivities(this, PackageManager.MATCH_ALL)
//                activities.for
//            }

            intent.action?.let {
                isMod = when (it) {
                    "android.settings.SETTINGS" -> false
                    "android.service.quicksettings.action.QS_TILE_PREFERENCES" -> true
                    else -> it.startsWith("android.settings")
                }
                if (isMod) return@run
            }
            intent.component?.let {
                isMod = when (it.packageName) {
                    "com.android.settings" -> true
                    "com.android.phone" -> true
                    else -> false
                }
            }
        }
        return isMod
    }

}

fun ActivityInfo.isResizeableModeExt(): Boolean {
    val mode = ActivityInfoClass.field { name("resizeMode") }.get(this).int()
    return ActivityInfoClass.method { name("isResizeableMode") }.get().invoke<Boolean>(mode)
        ?: false
}
