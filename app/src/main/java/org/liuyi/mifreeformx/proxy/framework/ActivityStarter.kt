package org.liuyi.mifreeformx.proxy.framework

import android.content.Intent
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/21/23:10:13
 * @Description:
 */
interface ActivityStarter : ProxyInterface {

    interface Request : ProxyInterface {

        @get:ProxyField(name = "intent")
        @set:ProxyField(name = "intent")
        var intent: Intent?

        @get:ProxyField(name = "activityOptions")
        @set:ProxyField(name = "activityOptions")
        var activityOptions: SafeActivityOptions?
    }
}