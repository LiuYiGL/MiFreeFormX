package org.liuyi.mifreeformx.proxy.systemui

import android.app.PendingIntent
import android.content.ComponentName
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/09/15:26:20
 * @Description:
 */
interface AppMiniWindowManager : ProxyInterface {

    @get:ProxyField(name = "mTopActivity")
    @set:ProxyField(name = "mTopActivity")
    var mTopActivity: ComponentName?

    @ProxyMethod(name = "launchMiniWindowActivity")
    fun launchMiniWindowActivity(str: String?, pendingIntent: PendingIntent?)

    @ProxyMethod(name = "canNotificationSlide")
    fun canNotificationSlide(str: String?, pendingIntent: PendingIntent?): Boolean
}