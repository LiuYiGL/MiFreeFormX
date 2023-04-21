package org.liuyi.mzfreeform.utils

import android.app.ActivityOptions
import android.content.Context
import android.graphics.Rect
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import org.liuyi.mzfreeform.xposed.hooker.AndroidHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/04/19/9:30:53
 * @Description:
 */
object MiuiMultiWindowUtils {

    private val clazz by lazy { "android.util.MiuiMultiWindowUtils".toClass() }

    /**
     * 获取小窗Rect
     *
     * @param context
     * @return
     */
    fun getFreeformRect(context: Context): Rect = clazz.method {
        name("getFreeformRect")
        paramCount(1)
    }.get().call(context) as Rect

    /**
     * 获取小窗ActivityOptions
     *
     * @param context
     * @param packageName
     * @return
     */
    fun getActivityOptions(context: Context, packageName: String): ActivityOptions {
        var activityOptions: ActivityOptions? = null
        try {
            activityOptions = "android.util.MiuiMultiWindowUtils".toClass().method {
                name = "getActivityOptions"
                // _, 包名, noCheck, isMiniFreeformMode
                param(ContextClass, StringClass, BooleanClass, BooleanClass)
            }.get().call(context, packageName, true, false) as ActivityOptions?
        } catch (e: Exception) {
            loggerD(msg = "MiuiMultiWindowUtils getActivityOptions error")
        }
        if (activityOptions == null) {
            activityOptions = ActivityOptions.makeBasic()!!
            ActivityOptions::class.java.method {
                name = "setLaunchWindowingMode"
            }.get(activityOptions).call(5)
        }
        return activityOptions
    }
}