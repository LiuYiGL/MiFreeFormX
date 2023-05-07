package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/11:07:18
 * @Description:
 */
interface MiuiFreeFormActivityStack : ProxyInterface {

    @get:ProxyField(name = "mIsLaunchingSmallFreeForm")
    @set:ProxyField(name = "mIsLaunchingSmallFreeForm")
    var mIsLaunchingSmallFreeForm: Boolean

    @get:ProxyField(name = "mMiuiFreeFromWindowMode")
    @set:ProxyField(name = "mMiuiFreeFromWindowMode")
    var mMiuiFreeFromWindowMode: Int

    @get:ProxyField(name = "mCornerPosition")
    @set:ProxyField(name = "mCornerPosition")
    var mCornerPosition: Int

    @get:ProxyField(name = "mTask")
    @set:ProxyField(name = "mTask")
    var mTask: Task?

    @get:ProxyField(name = "mTaskAnimationAdapter")
    @set:ProxyField(name = "mTaskAnimationAdapter")
    var mTaskAnimationAdapter: Any?

    @ProxyMethod(name = "inPinMode")
    fun inPinMode(): Boolean
}