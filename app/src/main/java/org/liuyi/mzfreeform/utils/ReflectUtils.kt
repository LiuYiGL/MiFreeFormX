package org.liuyi.mifreeformx.utils

import com.highcapable.yukihookapi.hook.factory.field

/**
 * @Author: Liuyi
 * @Date: 2023/04/22/14:57:42
 * @Description:
 */
class ReflectUtils {
}

fun Any.getFieldValueOrNull(fieldName: String) = javaClass.field { name(fieldName) }.get(this).any()
fun Any.setFieldValue(fieldName: String, value: Any?) =
    javaClass.field { name(fieldName) }.get(this).set(value)
