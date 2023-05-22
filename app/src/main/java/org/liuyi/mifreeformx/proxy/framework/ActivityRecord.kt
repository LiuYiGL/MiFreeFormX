package org.liuyi.mifreeformx.proxy.framework

import android.content.ComponentName
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod

/**
 * @Author: Liuyi
 * @Date: 2023/05/23/1:10:48
 * @Description:
 */
interface ActivityRecord : WindowContainer {

    @get:ProxyField(name = "mActivityComponent")
    val mActivityComponent: ComponentName?

    @ProxyMethod(name = "reparent")
    fun reparent(newTaskFrag: TaskFragment?, position: Int, reason: String?)

    @ProxyMethod(name = "removeFromHistory")
    fun removeFromHistory(reason: String?)
}