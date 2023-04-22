package org.liuyi.mzfreeform.utils

import android.content.ComponentName
import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mzfreeform.xposed.hooker.SystemUiHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/04/21/23:47:17
 * @Description:
 */
class MiuiActivityUtil {
    private val miuiActivityUtilClass by lazy {
        "com.miui.systemui.util.MiuiActivityUtil".toClass()
    }
    private val dependencyClass by lazy {
        "com.android.systemui.Dependency".toClass()
    }

    private fun getInstance() =
        dependencyClass.method { name("get") }.get().call(miuiActivityUtilClass)

    fun getTopActivity() =
        miuiActivityUtilClass.method { name("getTopActivity") }.get(getInstance())
            .invoke<ComponentName>()



}