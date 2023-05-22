package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/23/6:33:29
 * @Description:
 */
interface RecentTasks: ProxyInterface {

    @ProxyMethod(name = "add")
    fun add(task: Task?)

    @ProxyMethod(name = "remove")
    fun remove(task: Task?)
}