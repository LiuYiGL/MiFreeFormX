package org.liuyi.mifreeformx.proxy.framework

import android.app.ActivityManager.RunningTaskInfo
import android.content.Context
import android.content.Intent
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/7:35:31
 * @Description:
 */
interface ActivityTaskManagerService : ProxyInterface {

    @get:ProxyField(name = "mMiuiFreeFormManagerService")
    val mMiuiFreeFormManagerService: MiuiFreeFormManagerService?

    @get: ProxyField(name = "mContext")
    val mContext: Context?

    @get:ProxyField(name = "mRootWindowContainer")
    val mRootWindowContainer: RootWindowContainer?

    @ProxyMethod(name = "getTasks", paramCount = "1")
    fun getTasks(maxNum: Int): List<RunningTaskInfo?>?

    @ProxyMethod(name = "getTopTaskVisibleActivities")
    fun getTopTaskVisibleActivities(): List<Intent?>?
}