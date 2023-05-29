package org.liuyi.mifreeformx.proxy.framework

import android.util.DisplayMetrics
import android.view.DisplayCutout
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/18/9:49:26
 * @Description:
 */
interface DisplayContent : ProxyInterface {

    @get:ProxyField(name = "mDisplayPolicy")
    val mDisplayPolicy: DisplayPolicy?

    @get:ProxyField(name = "mInitialDisplayCutout", isFindInSuper = true)
    @set:ProxyField(name = "mInitialDisplayCutout", isFindInSuper = true)
    var mInitialDisplayCutout: DisplayCutout?

    @get:ProxyField(name = "mRealDisplayMetrics")
    val mRealDisplayMetrics: DisplayMetrics?

    @get:ProxyField(name = "mDisplayMetrics", isFindInSuper = true)
    val mDisplayMetrics: DisplayMetrics?

    @ProxyMethod(name = "getDisplayId", isFindInSuper = true)
    fun getDisplayId(): Int
}