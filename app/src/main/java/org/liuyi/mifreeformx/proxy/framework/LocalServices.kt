package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass
import org.liuyi.mifreeformx.xposed.hooker.FreeformLoseFocusHooker.getProxyAs

/**
 * @Author: Liuyi
 * @Date: 2023/05/17/10:05:48
 * @Description:
 */
interface LocalServices : ProxyInterface {

    companion object {
        val StaticProxy by lazy { "com.android.server.LocalServices".toClass().getProxyAs<LocalServices>() }
    }

    @ProxyMethod(name = "getService")
    fun getService(type: Class<*>?): Any?

}