package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/14/9:43:26
 * @Description:
 */
interface MiuiFreeFormGestureController: ProxyInterface {

    @get:ProxyField(name = "mGestureListener")
    var mGestureListener: Any?

    @get:ProxyField(name = "mService")
    var mService: WindowManagerService?

    @get:ProxyField(name = "mMiuiFreeFormManagerService")
    var mMiuiFreeFormManagerService: MiuiFreeFormManagerService?

    @ProxyMethod(name = "notifyFreeFormApplicationResizeEnd")
    fun notifyFreeFormApplicationResizeEnd(resizeTime: Long, mffas: MiuiFreeFormActivityStack?)

}