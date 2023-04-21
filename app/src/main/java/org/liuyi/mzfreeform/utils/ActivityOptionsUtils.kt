package org.liuyi.mzfreeform.utils

import android.app.ActivityOptions
import android.os.Bundle
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.method

/**
 * @Author: Liuyi
 * @Date: 2023/04/20/5:28:02
 * @Description:
 */
object ActivityOptionsUtils {

    private val clazz by lazy { ActivityOptions::class.java }

    fun constructor(bundle: Bundle): ActivityOptions = clazz.constructor {
        paramCount(1)
    }.get().newInstance<ActivityOptions>(bundle) as ActivityOptions

}

fun ActivityOptions.setLaunchWindowingModeExt(mode: Int) =
    ActivityOptions::class.java.method {
        name("setLaunchWindowingMode")
    }.get(this).call(mode)
