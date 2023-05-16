package org.liuyi.mifreeformx.proxy.framework

import android.graphics.Rect
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/14/9:49:51
 * @Description:
 */
interface MiuiFreeFormWindowMotionHelper: ProxyInterface {

    @ProxyMethod(name = "setLeashPositionAndScale")
    fun setLeashPositionAndScale(currentPosition: Rect, stack: MiuiFreeFormActivityStack)

    @ProxyMethod(name = "finishAnimationControl")
    fun finishAnimationControl(stack: MiuiFreeFormActivityStack?, bound: Rect?, scale: Float, mode: Int)

    @ProxyMethod(name = "findControlFreeFormActivityStack")
    fun findControlFreeFormActivityStack(x: Float, y: Float): MiuiFreeFormActivityStack?
}