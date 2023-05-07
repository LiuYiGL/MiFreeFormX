package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/7:35:31
 * @Description:
 */
interface ActivityTaskManagerService : ProxyInterface {

    @get:ProxyField(name = "mMiuiFreeFormManagerService")
    val mMiuiFreeFormManagerService: MiuiFreeFormManagerService?
}