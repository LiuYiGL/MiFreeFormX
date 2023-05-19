package org.liuyi.mifreeformx.proxy.framework

import android.os.IBinder
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass
import org.liuyi.mifreeformx.xposed.hooker.FreeformLoseFocusHooker.getProxyAs

/**
 * @Author: Liuyi
 * @Date: 2023/05/19/10:35:05
 * @Description:
 */
interface ServiceManager : ProxyInterface {

    companion object{
        val StaticProxy by lazy { "android.os.ServiceManager".toClass().getProxyAs<ServiceManager>() }
    }

    @ProxyMethod(name = "getService")
    fun getService(name: String?): IBinder?
}