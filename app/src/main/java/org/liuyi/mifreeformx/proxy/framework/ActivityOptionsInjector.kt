package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/15/11:11:08
 * @Description:
 */
interface ActivityOptionsInjector: ProxyInterface {

    @get: ProxyField(name = "mFreeformScale")
    @set: ProxyField(name = "mFreeformScale")
    var mFreeformScale: Float
}