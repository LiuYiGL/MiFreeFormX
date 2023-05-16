package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/16/15:25:48
 * @Description:
 */
interface MiuiFreeFormGesturePointerEventListener : ProxyInterface {

    @get:ProxyField(name = "mFreeFormWindowMotionHelper")
    val mFreeFormWindowMotionHelper: MiuiFreeFormWindowMotionHelper

    @get:ProxyField(name = "mInMultiTouch")
    val mInMultiTouch: Boolean

    @get:ProxyField(name = "mGestureController")
    var mGestureController: MiuiFreeFormGestureController?

    @ProxyMethod(name = "startExitApplication")
    fun startExitApplication(mffas: MiuiFreeFormActivityStack?)
}