package org.liuyi.mifreeformx.xposed.hooker.systemui

import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.bean.BlackListBean
import org.liuyi.mifreeformx.proxy.systemui.AppMiniWindowManager
import org.liuyi.mifreeformx.proxy.systemui.Dependency
import org.liuyi.mifreeformx.proxy.systemui.MiuiExpandableNotificationRow
import org.liuyi.mifreeformx.proxy.systemui.MiuiStatusBarNotificationActivityStarter
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/23/23:09:06
 * @Description:
 */
object ClickNotificationHooker : LyBaseHooker() {

    val OPEN_MODE = PrefsData("click_notification_open_window_mode", 0)
    val OPEN_MODE_TEXT = listOf("关闭", "开启", "白名单模式", "黑名单模式")
    val SELECTED_APPS_SET_PREF_DATA = PrefsData("click_notification_selected_apps", setOf<String>())
    val IGNORE_TOP_SELECTED_APPS_SET_PREF_DATA =
        PrefsData("single_click_notification_ignore_top_selected_apps", setOf("com.miui.home"))

    /**
     * 单击通知小窗打开时跳过锁屏状态
     */
    val OPEN_NOTICE_SKIP_LOCKSCREEN = PrefsData("open_notice_skip_lockscreen", true)

    val SELECTED_APPS_LIST = object : BlackListBean(SELECTED_APPS_SET_PREF_DATA) {}

    val IGNORE_TOP_SELECTED_APPS_LIST = object : BlackListBean(IGNORE_TOP_SELECTED_APPS_SET_PREF_DATA) {}

    override fun onHook() {

        val aMWMClass = "com.android.systemui.statusbar.notification.policy.AppMiniWindowManager".toClass()

        /**
         * Hook com.android.systemui.statusbar.phone.MiuiStatusBarNotificationActivityStarter#startNotificationIntent
         * 当点击通知时会触发这个方法，可从第一个参数 获取PendingIntent 然后反射getIntent 获得Intent
         */
        "com.android.systemui.statusbar.phone.MiuiStatusBarNotificationActivityStarter".hook {
            injectMember {
                method { name("startNotificationIntent") }
                beforeHook {
                    val mode = prefs.get(OPEN_MODE)
                    if (mode == 0) return@beforeHook
                    logD("模式：${OPEN_MODE_TEXT[mode]}参数：${args.asList()}")
                    if (prefs.get(OPEN_NOTICE_SKIP_LOCKSCREEN)) {
                        val starter = instance.getProxyAs<MiuiStatusBarNotificationActivityStarter>()
                        val mContext = starter.mContext ?: return@beforeHook
                        val keyguardManager = mContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                        val isKeyguardLocked = keyguardManager.isKeyguardLocked
                        logD("锁屏状态：$isKeyguardLocked")
                        if (isKeyguardLocked) {
                            return@beforeHook
                        }
                    }
                    val pendingIntent = args[0] as PendingIntent?
                    pendingIntent ?: return@beforeHook
                    takeIf {
                        when (mode) {
                            1 -> true
                            2 -> SELECTED_APPS_LIST.contains(prefs, pendingIntent.creatorPackage)
                            3 -> !SELECTED_APPS_LIST.contains(prefs, pendingIntent.creatorPackage)
                            else -> false
                        }
                    }?.run {
                        // 逻辑开始
                        val appMiniWindowManager = Dependency.Proxy.get(aMWMClass).getProxyAs<AppMiniWindowManager>()
                        val mTopWindowPackage = appMiniWindowManager.mTopActivity
                        logD("当前顶部应用：$mTopWindowPackage")
                        if (IGNORE_TOP_SELECTED_APPS_LIST.contains(prefs, mTopWindowPackage?.packageName)) {
                            return@beforeHook
                        }

                        args[3]?.getProxyAs<MiuiExpandableNotificationRow>()?.let {
                            val targetPkg = it.getMiniWindowTargetPkg()
                            if (!appMiniWindowManager.canNotificationSlide(targetPkg, pendingIntent)) {
                                logD("当前通知不可下滑，取消操作")
                                return@beforeHook
                            }
                            logD("使用小窗打开通知")
                            appMiniWindowManager.launchMiniWindowActivity(targetPkg, pendingIntent)
                            resultNull()
                        }
                    }
                }
            }
        }
    }
}