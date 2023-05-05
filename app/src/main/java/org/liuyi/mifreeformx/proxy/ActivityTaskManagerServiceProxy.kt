package org.liuyi.mifreeformx.proxy

import com.highcapable.yukihookapi.hook.factory.field
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/04/3:52:28
 * @Description:
 */

class ActivityTaskManagerServiceProxy(private val instance: Any) {

    companion object {
        val clazz by lazy { "com.android.server.wm.ActivityTaskManagerService".toClass() }

    }

    internal val mMiuiFreeFormManagerService
        get() = clazz.field { name("mMiuiFreeFormManagerService") }.get(instance).any()

    internal val mWindowManager
        get() = clazz.field { name("mWindowManager") }.get(instance).any()
}