package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/22/2:45:08
 * @Description:
 */
interface ActivityTaskSupervisor : ProxyInterface {

    @get:ProxyField(name = "mService")
    val mService: ActivityTaskManagerService?
}