package org.liuyi.mzfreeform.utils

import android.app.Activity
import android.content.ComponentName
import android.os.IBinder
import android.util.ArrayMap
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.highcapable.yukihookapi.hook.type.android.ActivityThreadClass

/**
 * @Author: Liuyi
 * @Date: 2023/04/22/6:14:23
 * @Description:
 */
object ActivityUtils {

    internal fun getTopActivityByThread(): Activity? {
        val activityThread = ActivityThreadClass
            .method { name("currentActivityThread") }.get().call()

        val mActivities = activityThread?.javaClass
            ?.field { name("mActivities") }?.get(activityThread)?.cast<ArrayMap<IBinder, *>>()

        mActivities?.let { activities ->
            for (entry in activities) {
                val isPaused = entry.javaClass.field { name("paused") }.get(entry).boolean()
                if (!isPaused) {
                    return entry.javaClass.field { name("activity") }.get(entry).cast<Activity>()
                }
            }
        }
        return null
    }

    internal val getTopActivityByCommonUtil: () -> ComponentName? = {
        // com.miui.systemui.util.CommonUtil#getTopActivity
        kotlin.runCatching {
            "com.miui.systemui.util.CommonUtil".toClass()
                .method { name("getTopActivity") }.get().call() as ComponentName?
        }.getOrNull()
    }
}