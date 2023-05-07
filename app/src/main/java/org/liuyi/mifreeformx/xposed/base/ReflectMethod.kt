package org.liuyi.mifreeformx.xposed.base

import java.lang.reflect.Method

open class ReflectMethod<T>(init: () -> Method) {

    internal val method = init()

    internal fun call(instance: Any?, vararg args: Any?) =
        method.invoke(instance, *args) as? T


    internal operator fun invoke(instance: Any?, vararg args: Any?) = call(instance, args)
}
