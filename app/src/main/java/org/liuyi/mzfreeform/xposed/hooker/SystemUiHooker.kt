package org.liuyi.mifreeformx.xposed.hooker

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.intent_extra.forceFreeFromMode
import org.liuyi.mifreeformx.utils.*

/**
 * @Author: Liuyi
 * @Date: 2023/04/19/23:27:09
 * @Description: 可用作状态栏上的各种小窗打开，点击通知、长按快捷方式
 */
object SystemUiHooker : YukiBaseHooker() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onHook() {
        var context: Context? = null

        val commonUtilClass = "com.miui.systemui.util.CommonUtil".toClass()
        val getTopActivity: () -> ComponentName? = {
            commonUtilClass.method { name("getTopActivity") }.get().invoke<ComponentName>()
        }

        val coreStartableClass = "com.android.systemui.CoreStartable".toClass()
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
                    by(this, DataConst.LONG_PRESS_TILE) {
                        loggerD(msg = "${args.asList()}")
                        var intent = args[0] as? Intent?
                        context = context ?: coreStartableClass.field { name("mContext") }
                            .get(instance).any() as? Context?
                        if (intent != null && context != null) {
                            // 备份Intent，防止SB的系统用同一个实例吃遍天下
                            args[0] = Intent(intent)
                            intent = args[0] as Intent
                            val topActivity = getTopActivity()
                            val componentName = intent.resolveActivity(context!!.packageManager)
                            loggerD(msg = "$topActivity and $componentName")
                            // 如果是顶部App 则不处理
                            if (topActivity?.packageName == componentName.packageName) return@by
                            if (prefs.direct().get(DataConst.FORCE_CONTROL_ALL_OPEN)
                                || isTile(intent)
                            ) {
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
                    by(this, DataConst.OPEN_NOTICE) {
                        // 加载类
                        val dependencyClass = "com.android.systemui.Dependency".toClass()
                        val appMiniWindowManagerClass =
                            "com.android.systemui.statusbar.notification.policy.AppMiniWindowManager".toClass()

                        // 逻辑开始
                        val appMiniWindowManager = dependencyClass.method {
                            name("get")
                            param(Class::class.java)
                        }.get().call(appMiniWindowManagerClass)

                        appMiniWindowManagerClass.method { name("launchMiniWindowActivity") }
                            .get(appMiniWindowManager).let {
                                val targetPkg = args[3]?.javaClass
                                    ?.method { name("getMiniWindowTargetPkg") }
                                    ?.get(args[3])?.invoke<String?>()
                                it.call(targetPkg, args[0])
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
