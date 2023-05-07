package org.liuyi.mifreeformx.proxy.systemui

import android.content.Context
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface

/**
 * @Author: Liuyi
 * @Date: 2023/05/06/22:57:35
 * @Description:
 */
interface CentralSurfacesImpl : ProxyInterface {
    @get:ProxyField(name = "mContext", isFindInSuper = true)
    val mContext: Context?
}