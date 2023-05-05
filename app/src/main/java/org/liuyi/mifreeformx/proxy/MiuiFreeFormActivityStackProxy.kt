package org.liuyi.mifreeformx.proxy

import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass


/**
 * @Author: Liuyi
 * @Date: 2023/05/04/21:17:33
 * @Description:
 */
class MiuiFreeFormActivityStackProxy(val instance: Any) {
    companion object {
        val clazz by lazy { "com.android.server.wm.MiuiFreeFormActivityStack".toClass() }
    }

    internal var mIsLaunchingSmallFreeForm
        get() = clazz.field { name("mIsLaunchingSmallFreeForm") }.get(instance).boolean()
        set(value) = clazz.field { name("mIsLaunchingSmallFreeForm") }.get(instance).set(value)!!

    internal var mMiuiFreeFromWindowMode
        get() = clazz.field { name("mMiuiFreeFromWindowMode") }.get(instance).int()
        set(value) = clazz.field { name("mMiuiFreeFromWindowMode") }.get(instance).set(value)!!

    internal var mCornerPosition
        get() = clazz.field { name("mCornerPosition") }.get(instance).int()
        set(value) = clazz.field { name("mCornerPosition") }.get(instance).set(value)!!

    internal var mTask
        get() = clazz.field { name("mTask") }.get(instance).any()
        set(value) = clazz.field { name("mTask") }.get(instance).set(value)!!

    internal var mTaskAnimationAdapter
        get() = clazz.field { name("mTaskAnimationAdapter") }.get(instance).any()
        set(value) = clazz.field { name("mTaskAnimationAdapter") }.get(instance).set(value)!!

    internal fun getStackPackageName() =
        clazz.method { name("getStackPackageName") }.get(instance).string()

    internal fun inPinMode() =
        clazz.method { name("inPinMode") }.get(instance).boolean()

}