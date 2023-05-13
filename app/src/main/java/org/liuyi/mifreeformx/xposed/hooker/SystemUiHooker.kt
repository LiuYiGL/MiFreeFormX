package org.liuyi.mifreeformx.xposed.hooker

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.log.loggerD
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.intent_extra.forceFreeFromMode
import org.liuyi.mifreeformx.proxy.systemui.AppMiniWindowManager
import org.liuyi.mifreeformx.proxy.systemui.CentralSurfacesImpl
import org.liuyi.mifreeformx.proxy.systemui.CommonUtil
import org.liuyi.mifreeformx.proxy.systemui.Dependency
import org.liuyi.mifreeformx.proxy.systemui.MiuiExpandableNotificationRow
import org.liuyi.mifreeformx.utils.*
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/04/19/23:27:09
 * @Description: 可用作状态栏上的各种小窗打开，点击通知、长按快捷方式
 */
object SystemUiHooker : LyBaseHooker() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onHook() {
        var context: Context? = null

        /**
         * Hook com.android.systemui.statusbar.phone.CentralSurfacesImpl#startActivityDismissingKeyguard
         * (android.content.Intent, boolean, boolean, boolean, com.android.systemui.plugins.ActivityStarter.Callback,
         * int, com.android.systemui.animation.ActivityLaunchAnimator.Controller, android.os.UserHandle)
         *
         * 当 长按 Tile 时，或点击设置时触发方法，可在此进行判断
         */
        "com.android.systemui.statusbar.phone.CentralSurfacesImpl".hook {
            injectMember {
                method {
                    name("startActivityDismissingKeyguard")
                    paramCount(8)
                }
                beforeHook {
                    if (prefs.get(DataConst.LONG_PRESS_TILE)) {
                        loggerD(msg = "${args.asList()}")
                        var intent = args[0] as? Intent?
                        context = context ?: instance.getProxyAs<CentralSurfacesImpl>().mContext
                        if (intent != null && context != null) {
                            // 备份Intent，防止SB的系统用同一个实例吃遍天下
                            args[0] = Intent(intent)
                            intent = args[0] as Intent
                            val topActivity = CommonUtil.proxy.getTopActivity()
                            val componentName = intent.resolveActivity(context!!.packageManager)
                            loggerD(msg = "$topActivity and $componentName")
                            // 如果是顶部App 则不处理
                            if (topActivity?.packageName == componentName.packageName
                                || BlackList.TileBlacklist.contains(prefs, componentName.packageName)
                            ) return@beforeHook
                            if (prefs.get(DataConst.FORCE_CONTROL_ALL_OPEN) || isTile(intent)) {
                                intent.forceFreeFromMode()
                                var flag = args[5] as Int
                                flag = flag or Intent.FLAG_ACTIVITY_NEW_TASK
                                flag = flag or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                                flag = flag or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                                args[5] = flag
                            }
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
                    if (prefs.get(DataConst.OPEN_NOTICE)) {
                        // 加载类
                        val appMiniWindowManagerClass =
                            "com.android.systemui.statusbar.notification.policy.AppMiniWindowManager".toClass()
                        // 逻辑开始
                        val appMiniWindowManager =
                            Dependency.Proxy.get(appMiniWindowManagerClass).getProxyAs<AppMiniWindowManager>()

                        args[3]?.getProxyAs<MiuiExpandableNotificationRow>()?.let {
                            appMiniWindowManager.launchMiniWindowActivity(
                                it.getMiniWindowTargetPkg(), args[0] as PendingIntent
                            )
                            resultNull()
                        }
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
                    if (prefs.get(DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW)) {
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
                    if (prefs.get(DataConst.NOTIFY_LIMIT_REMOVE_SMALL_WINDOW)) {
                        resultTrue()
                    }
                }
            }
        }.by { false }

    }

    /**
     * 通过Intent 判断是否需要使用小窗打开
     *
     * @param intent
     * @return
     */
    private fun isTile(intent: Intent?): Boolean {
        return intent?.run {
            action?.let {
                if (it == "android.service.quicksettings.action.QS_TILE_PREFERENCES") true
                else if (it == "android.settings.SETTINGS") false
                else if (it.startsWith("android.settings")) true
                else null
            } ?: component?.let {
                if (it.packageName == "com.android.settings" || it.packageName == "com.android.phone") true
                else null
            } ?: `package`?.let {
                if (it.startsWith("com.android")) true
                else null
            }
        } ?: false
    }
}
