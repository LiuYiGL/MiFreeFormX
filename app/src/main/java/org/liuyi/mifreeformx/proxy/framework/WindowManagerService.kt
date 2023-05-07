package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/8:18:16
 * @Description:
 */
interface WindowManagerService : ProxyInterface {

    @get:ProxyField(name = "mMiuiFreeFormGestureController")
    val mMiuiFreeFormGestureController: Any?

    @ProxyMethod(name = "launchSmallFreeFormWindow")
    fun launchSmallFreeFormWindow(miuiFreeFormActivityStack: MiuiFreeFormActivityStack, boolean: Boolean)
}