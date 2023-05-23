package org.liuyi.mifreeformx.xposed.hooker

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.intent.LyIntent
import org.liuyi.mifreeformx.proxy.systemui.AppMiniWindowManager
import org.liuyi.mifreeformx.proxy.systemui.CentralSurfacesImpl
import org.liuyi.mifreeformx.proxy.systemui.CommonUtil
import org.liuyi.mifreeformx.proxy.systemui.Dependency
import org.liuyi.mifreeformx.proxy.systemui.MiuiExpandableNotificationRow
import org.liuyi.mifreeformx.proxy.systemui.MiuiStatusBarNotificationActivityStarter
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
