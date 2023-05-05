package org.liuyi.mifreeformx.proxy

import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/05/1:53:37
 * @Description:
 */
class MiuiFreeFormManagerProxy {
    companion object {
        val clazz by lazy { "miui.app.MiuiFreeFormManager".toClass() }

        internal fun getService() = clazz.method { name("getService") }.get().call()
    }
}