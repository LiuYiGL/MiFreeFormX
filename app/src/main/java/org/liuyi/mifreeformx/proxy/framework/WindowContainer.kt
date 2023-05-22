package org.liuyi.mifreeformx.proxy.framework

import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import java.util.ArrayList

/**
 * @Author: Liuyi
 * @Date: 2023/05/23/1:54:18
 * @Description:
 */
interface WindowContainer : ProxyInterface {

    @get:ProxyField("mChildren", isFindInSuper = true)
    val mChildren: ArrayList<Any?>?
}