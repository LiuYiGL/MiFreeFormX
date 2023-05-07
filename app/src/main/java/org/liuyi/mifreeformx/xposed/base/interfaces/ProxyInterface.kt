package org.liuyi.mifreeformx.xposed.base.interfaces

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/8:39:19
 * @Description:
 */
interface ProxyInterface {

    var instance: Any?

    val clazz: Class<*>
}