package org.liuyi.mifreeformx.proxy.systemui

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/09/15:35:06
 * @Description:
 */
interface MiuiExpandableNotificationRow: ProxyInterface {

    @ProxyMethod(name = "getMiniWindowTargetPkg")
    fun getMiniWindowTargetPkg(): String?
}