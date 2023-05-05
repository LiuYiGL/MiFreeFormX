package org.liuyi.mifreeformx.proxy

import android.content.Intent
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/04/5:01:30
 * @Description:
 */
class TaskProxy(private val instance: Any) {

    companion object {
        private val clazz by lazy { "com.android.server.wm.Task".toClass() }
    }

    internal var mTaskId
        get() = clazz.field { name("mTaskId") }.get(instance).int()
        set(value) = clazz.field { name("mTaskId") }.get(instance).set(value)!!

    internal val mAtmService
        get() = clazz.field {
            name("mAtmService")
            superClass()
        }.get(instance).any()

    internal val mWmService
        get() = clazz.field {
            name("mWmService")
            superClass()
        }.get(instance).any()

    internal fun getBaseIntent() =
        clazz.method { name("getBaseIntent") }.get(instance).invoke<Intent>()

    internal fun getActivityType() =
        clazz.method {
            name("getActivityType")
            superClass()
        }.get(instance).int()

}

