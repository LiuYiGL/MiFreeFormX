package org.liuyi.mifreeformx.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.param.HookParam
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.highcapable.yukihookapi.hook.type.android.ActivityInfoClass
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData

/**
 * @Author: Liuyi
 * @Date: 2023/04/20/15:46:52
 * @Description:
 */

const val KEY_LAUNCH_BOUNDS = "android:activity.launchBounds"
const val EXTRA_USER_HANDLE = "android.intent.extra.user_handle"
const val KEY_LAUNCH_WINDOWING_MODE = "android.activity.windowingMode"

fun PendingIntent.getIntentExt() =
    PendingIntent::class.java.method { name("getIntent") }.get(this).call() as Intent

fun Bundle.toMultiWidow(context: Context? = null): Bundle {
    putInt(KEY_LAUNCH_WINDOWING_MODE, 5)
    context?.let {
        val rect = MiuiMultiWindowUtils.getFreeformRect(context)
        putParcelable(KEY_LAUNCH_BOUNDS, rect)
    }
    return this
}

fun PackageParam.by(
    hookParam: HookParam,
    prefsData: PrefsData<Boolean>,
    block: HookParam.() -> Unit
) {
    if (prefs.direct().get(prefsData)) {
        block(hookParam)
    }
}

fun PackageParam.byAll(
    hookParam: HookParam, vararg pds: PrefsData<Boolean>, block: HookParam.() -> Unit
) {
    if (pds.all { prefs.direct().get(it) }) {
        block(hookParam)
    }
}

fun PackageParam.byAny(
    hookParam: HookParam, vararg pds: PrefsData<Boolean>, block: HookParam.() -> Unit
) {
    if (pds.any { prefs.direct().get(it) }) {
        block(hookParam)
    }
}

fun ActivityInfo.isResizeableModeExt(): Boolean {
    val mode = ActivityInfoClass.field { name("resizeMode") }.get(this).int()
    return ActivityInfoClass.method { name("isResizeableMode") }.get().invoke<Boolean>(mode)
        ?: false
}

fun Intent.isSameApp(callPackage: String) = component?.packageName == callPackage

internal fun Activity.startUriString(uriString: String) =
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))