package org.liuyi.mifreeformx.proxy

import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/05/1:08:34
 * @Description:
 */
class MiuiMultiWindowUtilsProxy(val instance: Any) {
    companion object {
        private val clazz by lazy { "android.util.MiuiMultiWindowUtils".toClass() }

        internal fun hasSmallFreeform() = clazz.method { name("hasSmallFreeform") }.get().boolean()

    }
}