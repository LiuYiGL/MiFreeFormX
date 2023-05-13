package org.liuyi.mifreeformx.proxy.systemui

import android.app.PendingIntent
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/09/15:26:20
 * @Description:
 */
interface AppMiniWindowManager : ProxyInterface {
    @ProxyMethod(name = "launchMiniWindowActivity")
    fun launchMiniWindowActivity(str: String?, pendingIntent: PendingIntent?)
}