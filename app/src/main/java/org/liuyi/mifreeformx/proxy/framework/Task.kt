package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/7:39:18
 * @Description:
 */
interface Task : ProxyInterface {
    @get:ProxyField(name = "mAtmService", isFindInSuper = true)
    val mAtmService: ActivityTaskManagerService?

    @get:ProxyField(name = "mWmService", isFindInSuper = true)
    val mWmService: WindowManagerService?

    @ProxyMethod(name = "getActivityType", isFindInSuper = true)
    fun getActivityType(): Int

}