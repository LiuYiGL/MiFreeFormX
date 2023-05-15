package org.liuyi.mifreeformx.proxy.framework

import android.content.res.Configuration
import android.graphics.Rect
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/7:39:18
 * @Description:
 */
interface Task : ProxyInterface {
    @get:ProxyField(name = "mAtmService", isFindInSuper = true)
    val mAtmService: ActivityTaskManagerService?

    @get:ProxyField(name = "mWmService", isFindInSuper = true)
    val mWmService: WindowManagerService?

    @get:ProxyField(name = "mFreeFormLaunchBoundsFromOptions")
    @set:ProxyField(name = "mFreeFormLaunchBoundsFromOptions")
    var mFreeFormLaunchBoundsFromOptions: Rect?

    @get:ProxyField(name = "mTaskId")
    val mTaskId: Int

    @ProxyMethod(name = "isActivityTypeHome", isFindInSuper = true)
    fun isActivityTypeHome(): Boolean

    @ProxyMethod(name = "getWindowingMode", isFindInSuper = true)
    fun getWindowingMode(): Int

    @ProxyMethod(name = "getActivityType", isFindInSuper = true)
    fun getActivityType(): Int

    @ProxyMethod(name = "getRootTask", isFindInSuper = true)
    fun getRootTask(): Task?

    @ProxyMethod(name = "getRootTaskId")
    fun getRootTaskId(): Int

    @ProxyMethod(name = "moveToFront", paramCount = "1")
    fun moveToFront(reason: String?)

    @ProxyMethod(name = "moveTaskToBack", paramCount = "1")
    fun moveTaskToBack(tr: Task?): Boolean

    @ProxyMethod(name = "getConfiguration", isFindInSuper = true)
    fun getConfiguration(): Configuration?

    @ProxyMethod(name = "inMultiWindowMode", isFindInSuper = true)
    fun inMultiWindowMode(): Boolean
}