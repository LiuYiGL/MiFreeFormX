package org.liuyi.mzfreeform.xposed.hooker

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ActivityInfoClass
import com.highcapable.yukihookapi.hook.type.android.ActivityManagerClass
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.utils.MiuiMultiWindowUtils

/**
 * @Author: Liuyi
 * @Date: 2023/04/19/23:27:09
 * @Description: 可用作状态栏上的各种小窗打开，点击通知、长按快捷方式
 */
object SystemUiHooker : YukiBaseHooker() {

    private const val KEY_LAUNCH_WINDOWING_MODE = "android.activity.windowingMode"
    private const val KEY_LAUNCH_BOUNDS = "android:activity.launchBounds"
    private const val EXTRA_USER_HANDLE = "android.intent.extra.user_handle"

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
                    result<Bundle>()?.apply {
                        putInt(KEY_LAUNCH_WINDOWING_MODE, 5)
                        context?.let {
                            val rect = MiuiMultiWindowUtils.getFreeformRect(it)
                            putParcelable(KEY_LAUNCH_BOUNDS, rect)
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
                        loggerD(msg = "intent: $this")
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


        /**
         * 解除小窗展开通知限制
         */
        "com.android.systemui.statusbar.notification.NotificationSettingsManager".hook {
            injectMember {
                method { name("canSlide") }
                replaceToTrue()
            }
        }.by { prefs.get(DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW) }

        /**
         * Hook com.android.systemui.statusbar.notification.policy.MiniWindowPolicy#isTopSamePackage
         * Intent
         * ComponentName
         * 优化 前台应用判断逻辑
         */
        "com.android.systemui.statusbar.notification.policy.MiniWindowPolicy".hook {
            injectMember {
                method { name("isTopSamePackage") }
                afterHook {
                    if (result == true) {
                        val intent = args[0] as Intent?
                        val currentUid =
                            ActivityManagerClass.method { name("getCurrentUser") }.get()
                                .invoke<Int>()
                        val targetUid = intent?.getIntExtra(EXTRA_USER_HANDLE, -1)
                        loggerD(msg = "当前用户id：$currentUid")
                        loggerD(msg = "目标用户id：$targetUid")
                        if (currentUid == null && targetUid == null) {
                            return@afterHook
                        }
                        if (currentUid != targetUid) {
                            result = false
                        }
                    }
                }
            }
            injectMember {
                method { name("isTopSameClass") }
                afterHook {
                    if (result == true) {
                        val intent = args[0] as Intent?
                        val currentUid =
                            ActivityManagerClass.method { name("getCurrentUser") }.get()
                                .invoke<Int>()
                        val targetUid = intent?.getIntExtra(EXTRA_USER_HANDLE, -1)
                        loggerD(msg = "当前用户id：$currentUid")
                        loggerD(msg = "目标用户id：$targetUid")
                        if (currentUid == null && targetUid == null) {
                            return@afterHook
                        }
                        if (currentUid != targetUid) {
                            result = false
                        }
                    }
                }
            }
        }.by { false }
    }

    /**
     * 通过Intent 判断是否需要使用小窗打开
     *
     * @param context
     * @param intent
     * @return
     */
    private fun nextIsModByIntent(context: Context? = null, intent: Intent?): Boolean {
        var isMod = false
        if (prefs.get(DataConst.FORCE_CONTROL_ALL_OPEN)) {
            return true
        }
        intent?.run {
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
                if (isMod) return@run
            }
            intent.`package`?.let {
                isMod = it.startsWith("com.android")
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
