package org.liuyi.mifreeformx.proxy.systemui

import android.content.ComponentName
import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.base.ReflectStaticMethod
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/06/23:14:28
 * @Description:
 */
object CommonUtil {
    val proxy = SystemUiHooker.getProxy<CommonUtil>("com.miui.systemui.util.CommonUtil".toClass())

    interface CommonUtil : ProxyInterface {

        @ProxyMethod(name = "getTopActivity")
        fun getTopActivity(): ComponentName?
    }
}