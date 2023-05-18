package org.liuyi.mifreeformx.proxy.framework

import android.view.GestureDetector
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/18/9:52:59
 * @Description:
 */
interface SystemGesturesPointerEventListener : ProxyInterface {

    @get:ProxyField(name = "mSwipeFireable")
    val mSwipeFireable: Boolean

    @get:ProxyField(name = "mGestureDetector")
    val mGestureDetector: GestureDetector?
}