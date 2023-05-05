package org.liuyi.mifreeformx.proxy

import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.IntType
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/04/20:58:36
 * @Description:
 */
class MiuiFreeFormManagerServiceProxy(val instance: Any) {
    companion object {
        val clazz by lazy { "com.android.server.wm.MiuiFreeFormManagerService".toClass() }
    }


    internal val mFreeFormActivityStacks =
        clazz.field { name("mFreeFormActivityStacks") }.get(instance).any()


    internal fun getMiuiFreeFormActivityStack(taskId: Int) = clazz.method {
        name("getMiuiFreeFormActivityStack")
        param(IntType)
    }.get(instance).call(taskId)


    internal fun getAllFreeFormStackInfosOnDisplay(displayId: Int) = clazz.method {
        name("getAllFreeFormStackInfosOnDisplay")
    }.get(instance).invoke<List<*>>(displayId)!!

}