package org.liuyi.mifreeformx.utils

import com.highcapable.yukihookapi.hook.core.finder.members.FieldFinder
import com.highcapable.yukihookapi.hook.core.finder.members.MethodFinder
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method

/**
 * @Author: Liuyi
 * @Date: 2023/04/22/14:57:42
 * @Description:
 */
class ReflectUtils {
}

internal fun Any.getFieldValueOrNull(fieldName: String) =
    javaClass.field { name(fieldName) }.get(this).any()

internal fun Any.getFieldValueByName(fieldName: String) =
    javaClass.field { name(fieldName) }.get(this).any()!!


internal fun Any.getFieldValue(initiate: FieldFinder.() -> Unit) =
    javaClass.field(initiate).get(this).any()

internal fun Any.setFieldValue(fieldName: String, value: Any?) =
    javaClass.field { name(fieldName) }.get(this).set(value)

internal fun Any.callMethodByName(methodName: String, vararg args: Any?) =
    javaClass.method { name(methodName) }.get(this).call(*args)

internal fun Any.callMethod(vararg args: Any?, initiate: MethodFinder.() -> Unit) =
    javaClass.method(initiate).get(this).call(*args)