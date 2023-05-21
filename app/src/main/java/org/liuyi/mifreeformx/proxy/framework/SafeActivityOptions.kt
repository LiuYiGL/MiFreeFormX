package org.liuyi.mifreeformx.proxy.framework

import android.app.ActivityOptions
import android.os.Bundle
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass
import org.liuyi.mifreeformx.xposed.hooker.FreeformLoseFocusHooker.getProxyAs

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/7:49:06
 * @Description:
 */
interface SafeActivityOptions : ProxyInterface {

    companion object {
        val StaticProxy = "com.android.server.wm.SafeActivityOptions".toClass().getProxyAs<SafeActivityOptions>()
    }

    @get:ProxyField(name = "mOriginalOptions")
    @set:ProxyField(name = "mOriginalOptions")
    var mOriginalOptions: ActivityOptions?

    @ProxyMethod(name = "fromBundle")
    fun fromBundle(bOptions: Bundle?): SafeActivityOptions?
}