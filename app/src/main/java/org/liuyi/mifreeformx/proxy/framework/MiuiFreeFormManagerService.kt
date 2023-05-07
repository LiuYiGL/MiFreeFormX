package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/11:36:34
 * @Description:
 */
interface MiuiFreeFormManagerService : ProxyInterface {

    @ProxyMethod(name = "getTopFreeFormActivityStack")
    fun getTopFreeFormActivityStack(): MiuiFreeFormActivityStack?

}