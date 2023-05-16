package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/11:36:34
 * @Description:
 */
interface MiuiFreeFormManagerService : ProxyInterface {

    @get:ProxyField(name = "mActivityTaskManagerService")
    var mActivityTaskManagerService: ActivityTaskManagerService?

    @ProxyMethod(name = "getTopFreeFormActivityStack")
    fun getTopFreeFormActivityStack(): MiuiFreeFormActivityStack?

    @ProxyMethod(name = "getMiuiFreeFormActivityStackForMiuiFB")
    fun getMiuiFreeFormActivityStackForMiuiFB(taskId: Int): MiuiFreeFormActivityStack?

    @ProxyMethod(name = "getAllMiuiFreeFormActivityStack")
    fun getAllMiuiFreeFormActivityStack(): List<Any?>
}