package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod

/**
 * @Author: Liuyi
 * @Date: 2023/05/23/1:10:48
 * @Description:
 */
interface ActivityRecord : WindowContainer {

    @ProxyMethod(name = "reparent")
    fun reparent(newTaskFrag: TaskFragment?, position: Int, reason: String?)
}