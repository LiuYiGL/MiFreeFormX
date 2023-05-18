package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/18/9:51:17
 * @Description:
 */
interface DisplayPolicy : ProxyInterface {

    @get:ProxyField(name = "mSystemGestures")
    val mSystemGestures: SystemGesturesPointerEventListener?
}