package org.liuyi.mifreeformx.proxy.systemui

import android.content.ComponentName
import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.base.ReflectStaticMethod
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/06/23:42:13
 * @Description:
 */
object DependencyProxy {
    internal val clazz by lazy { "com.android.systemui.Dependency".toClass() }


    internal val getTopActivity by lazy {
        ReflectStaticMethod<ComponentName> {
            clazz.method { name("getTopActivity") }.give()!!
        }
    }


}