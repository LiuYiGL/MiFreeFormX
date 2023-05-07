package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass
import org.liuyi.mifreeformx.xposed.hooker.FreeformLoseFocusHooker.getProxyAs

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/12:50:40
 * @Description:
 */
object MiuiMultiWindowUtils {

    val proxy = "android.util.MiuiMultiWindowUtils".toClass().getProxyAs<MiuiMultiWindowUtils>()

    interface MiuiMultiWindowUtils : ProxyInterface {

        @ProxyMethod(name = "hasSmallFreeform")
        fun hasSmallFreeform(): Boolean
    }
}