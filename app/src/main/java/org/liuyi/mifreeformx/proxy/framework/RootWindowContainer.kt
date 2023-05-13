package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/11/9:01:33
 * @Description:
 */
interface RootWindowContainer: ProxyInterface {

    @ProxyMethod(name = "getRootTask", paramCount = "1")
    fun getRootTask(rooTaskId: Int): Task?

    @ProxyMethod(name = "anyTaskForId", paramCount = "1")
    fun anyTaskForId(id: Int): Task?
}