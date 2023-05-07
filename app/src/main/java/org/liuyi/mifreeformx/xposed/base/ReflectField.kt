package org.liuyi.mifreeformx.xposed.base

import java.lang.reflect.Field

class ReflectField<T>(init: () -> Field) {

    internal val field = init()

    internal fun get(instance: Any) = field.get(instance) as? T

    internal fun set(instance: Any, value: T) = field.set(instance, value)

}
