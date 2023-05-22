package org.liuyi.mifreeformx.xposed.base

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ByteType
import com.highcapable.yukihookapi.hook.type.java.CharType
import com.highcapable.yukihookapi.hook.type.java.DoubleType
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ShortType
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField.Companion.getNameOrNull
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField.Companion.getTypeOrNull
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod.Companion.getNameOrNull
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod.Companion.getParamCountOrNull
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod.Companion.getParamOrNull
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod.Companion.getReturnTypeOrNull
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/6:05:52
 * @Description:
 */
object ReflectTools {

    fun findFieldByAnnotation(clazz: Class<*>, annotation: ProxyField) =
        clazz.field {
            annotation.getNameOrNull()?.let { name = it }
            annotation.getTypeOrNull()?.let { type = it.toPrimitiveTypeOrElse() }
            if (annotation.isFindInSuper) superClass(annotation.isOnlySuperClass)
        }

    internal fun findFieldByAnnotationOrNull(clazz: Class<*>, annotation: ProxyField) =
        kotlin.runCatching {
            findFieldByAnnotation(clazz, annotation)
        }.getOrNull()

    internal fun Class<*>.findFieldByAnnotation(annotation: ProxyField) = findFieldByAnnotation(this, annotation)

    fun findMethodByAnnotation(clazz: Class<*>, annotation: ProxyMethod) =
        clazz.method {
            annotation.getNameOrNull()?.let { name = it }
            annotation.getParamCountOrNull()?.let { paramCount = it }
            annotation.getParamOrNull()?.map { it.toPrimitiveTypeOrElse() }?.let { param(*it.toTypedArray()) }
            annotation.getReturnTypeOrNull()?.let { returnType = it }
            if (annotation.isFindInSuper) superClass(annotation.isOnlySuperClass)
        }

    internal fun Class<*>.findMethodByAnnotation(annotation: ProxyMethod) = findMethodByAnnotation(this, annotation)

    private fun String.toPrimitiveTypeOrElse() = when (this) {
        "int" -> IntType
        "boolean" -> BooleanType
        "char" -> CharType
        "byte" -> ByteType
        "short" -> ShortType
        "float" -> FloatType
        "double" -> DoubleType
        else -> this
    }
}