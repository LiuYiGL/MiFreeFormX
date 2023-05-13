package org.liuyi.mifreeformx.utils

import android.app.ActivityManager
import android.content.Context

/**
 * @Author: Liuyi
 * @Date: 2023/05/12/8:11:21
 * @Description:
 */
internal fun getPidFromPackageName(context: Context, packageName: String): Int {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningApps = activityManager.runningAppProcesses
    for (processInfo in runningApps) {
        if (processInfo.processName == packageName) {
            return processInfo.pid
        }
    }
    return -1
}

internal fun Context.getPidFromPackageNameExt(packageName: String) = getPidFromPackageName(this, packageName)