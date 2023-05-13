package org.liuyi.mifreeformx.proxy.systemui

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/06/23:42:13
 * @Description:
 */
object Dependency {

    val Proxy = SystemUiHooker.getProxy<Dependency>("com.android.systemui.Dependency".toClass())

    interface Dependency: ProxyInterface {

        @ProxyMethod(name = "get", param = "java.lang.Class")
        fun <T> get(cls: Class<T>?): T
    }


}