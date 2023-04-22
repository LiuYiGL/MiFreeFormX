package org.liuyi.mzfreeform.utils

import com.github.kyuubiran.ezxhelper.paramTypes
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType

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

fun Any.callMethod(methodName: String, vararg params: Any?): Any? {
    val map = params.asList().map {
        it?.javaClass ?: VagueType
    }
    return javaClass.method {
        name(methodName)
        paramTypes(*map.toTypedArray())
    }.get(this).call(params)
}

fun showCallStackInformation() =
    runCatching { throw Exception() }.exceptionOrNull()?.let { loggerE(e = it) }
