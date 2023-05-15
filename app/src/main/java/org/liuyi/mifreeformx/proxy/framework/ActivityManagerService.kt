package org.liuyi.mifreeformx.proxy.framework

import android.graphics.Rect
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/14/14:54:22
 * @Description:
 */
interface ActivityManagerService:ProxyInterface {

    @ProxyMethod(name = "resizeTask")
    fun resizeTask(taskId: Int, bounds: Rect?, resizeMode: Int)
}