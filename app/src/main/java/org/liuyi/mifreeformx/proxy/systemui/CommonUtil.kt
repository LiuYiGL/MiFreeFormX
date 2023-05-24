package org.liuyi.mifreeformx.proxy.systemui

import android.content.ComponentName
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker.getProxyAs
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/06/23:14:28
 * @Description:
 */
interface CommonUtil : ProxyInterface {

    companion object {
        val StaticProxy by lazy { "com.miui.systemui.util.CommonUtil".toClass().getProxyAs<CommonUtil>() }
    }

    @ProxyMethod(name = "getTopActivity")
    fun getTopActivity(): ComponentName?
}