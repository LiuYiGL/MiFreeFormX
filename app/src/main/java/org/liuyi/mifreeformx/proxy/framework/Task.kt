package org.liuyi.mifreeformx.proxy.framework

import android.content.ComponentName
import android.content.res.Configuration
import android.graphics.Rect
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/7:39:18
 * @Description:
 */
interface Task : TaskFragment {
    @get:ProxyField(name = "mAtmService", isFindInSuper = true)
    val mAtmService: ActivityTaskManagerService?

    @get:ProxyField(name = "mWmService", isFindInSuper = true)
    val mWmService: WindowManagerService?

    @get:ProxyField(name = "mFreeFormLaunchBoundsFromOptions")
    @set:ProxyField(name = "mFreeFormLaunchBoundsFromOptions")
    var mFreeFormLaunchBoundsFromOptions: Rect?

    @get:ProxyField(name = "mTaskId")
    val mTaskId: Int

    @get:ProxyField(name = "mUserId")
    val mUserId: Int

    @get:ProxyField(name = "realActivity")
    val realActivity: ComponentName?

    @get:ProxyField(name = "mParent", isFindInSuper = true)
    @set:ProxyField(name = "mParent", isFindInSuper = true)
    var mParent: Any?

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

    @ProxyMethod(name = "moveToFront", paramCount = "2")
    fun moveToFront(reason: String?, task: Task?)

    @ProxyMethod(name = "getTopResumedActivity")
    fun getTopResumedActivity(): ActivityRecord?

    @ProxyMethod(name = "getTopPausingActivity")
    fun getTopPausingActivity(): ActivityRecord?

    @ProxyMethod(name = "getTopNonFinishingActivity", isFindInSuper = true)
    fun getTopNonFinishingActivity(): ActivityRecord?

    @ProxyMethod(name = "addChild", paramCount = "2")
    fun addChild(child: WindowContainer?, index: Int)

    @ProxyMethod(name = "addChild", param = "com.android.server.wm.WindowContainer, int, boolean")
    fun addChild(child: WindowContainer?, position: Int, moveParents: Boolean)

    @ProxyMethod(name = "getPackageName")
    fun getPackageName(): String?

    @ProxyMethod(name = "moveTaskToBack", paramCount = "1")
    fun moveTaskToBack(tr: Task?): Boolean

    @ProxyMethod(name = "getConfiguration", isFindInSuper = true)
    fun getConfiguration(): Configuration?

    @ProxyMethod(name = "inMultiWindowMode", isFindInSuper = true)
    fun inMultiWindowMode(): Boolean
}