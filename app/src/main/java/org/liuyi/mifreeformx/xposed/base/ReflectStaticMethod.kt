package org.liuyi.mifreeformx.xposed.base

import java.lang.reflect.Method

/**
 * @Author: Liuyi
 * @Date: 2023/05/06/23:24:37
 * @Description:
 */
class ReflectStaticMethod<T>(init: () -> Method) {

    val method = init()

    internal fun call(vararg args: Any?) = method.invoke(null, *args) as? T

    internal operator fun invoke(vararg args: Any?) = call(null, args)
}