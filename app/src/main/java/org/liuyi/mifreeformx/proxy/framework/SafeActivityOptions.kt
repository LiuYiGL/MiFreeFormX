package org.liuyi.mifreeformx.proxy.framework

import android.app.ActivityOptions
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/7:49:06
 * @Description:
 */
interface SafeActivityOptions: ProxyInterface {

    @get:ProxyField(name = "mOriginalOptions")
    @set:ProxyField(name = "mOriginalOptions")
    var mOriginalOptions: ActivityOptions?
}