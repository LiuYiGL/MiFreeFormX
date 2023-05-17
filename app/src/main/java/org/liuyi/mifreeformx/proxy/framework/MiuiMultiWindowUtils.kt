package org.liuyi.mifreeformx.proxy.framework

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass
import org.liuyi.mifreeformx.xposed.hooker.FreeformLoseFocusHooker.getProxyAs

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/12:50:40
 * @Description:
 */
interface MiuiMultiWindowUtils : ProxyInterface {
    companion object {
        val StaticProxy = "android.util.MiuiMultiWindowUtils".toClass().getProxyAs<MiuiMultiWindowUtils>()
    }

    @get:ProxyField(name = "MULTI_WINDOW_SWITCH_ENABLED")
    @set:ProxyField(name = "MULTI_WINDOW_SWITCH_ENABLED")
    var MULTI_WINDOW_SWITCH_ENABLED: Boolean

    @ProxyMethod(name = "hasSmallFreeform")
    fun hasSmallFreeform(): Boolean

    @ProxyMethod(name = "getPossibleBounds")
    fun getPossibleBounds(
        context: Context?,
        vertical: Boolean,
        isFreeformLandscape: Boolean,
        packageName: String?
    ): RectF?

    @ProxyMethod(name = "getFreeformRect", paramCount = "1")
    fun getFreeformRect(context: Context?): Rect?

    @ProxyMethod(name = "getFreeformRect", paramCount = "3")
    fun getFreeformRect(context: Context?, needDisplayContentRotation: Boolean, isVertical: Boolean): Rect?

    @ProxyMethod(name = "getDisplaySize")
    fun getDisplaySize(context: Context?): Rect?

    @ProxyMethod(name = "getOriFreeformScale")
    fun getOriFreeformScale(context: Context?, isFreeformLandscape: Boolean): Float

    @ProxyMethod(name = "getScreenType")
    fun getScreenType(context: Context?): Int


    @ProxyMethod(name = "getFreeFormScale", paramCount = "3")
    fun getFreeFormScale(vertical: Boolean, landscape: Boolean, screenType: Int): Float

    // 小米13U 使用该方法
    @ProxyMethod(name = "getFreeFormScale", paramCount = "4")
    fun getFreeFormScale(context: Context?, vertical: Boolean, landscape: Boolean, screenType: Int): Float
}