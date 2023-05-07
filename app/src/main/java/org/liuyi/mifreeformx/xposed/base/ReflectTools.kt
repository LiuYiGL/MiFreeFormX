package org.liuyi.mifreeformx.xposed.base

import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
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
            if (annotation.name.isNotBlank()) name = annotation.name.trim()
            if (annotation.type.isNotBlank()) type = annotation.type.trim()
            if (annotation.isFindInSuper) superClass(annotation.isOnlySuperClass)
        }

    internal fun findFieldByAnnotationOrNull(clazz: Class<*>, annotation: ProxyField) =
        kotlin.runCatching {
            findFieldByAnnotation(clazz, annotation)
        }.getOrNull()

    internal fun Class<*>.findFieldByAnnotation(annotation: ProxyField) =
        ReflectTools.findFieldByAnnotation(this, annotation)

    fun findMethodByAnnotation(clazz: Class<*>, annotation: ProxyMethod) =
        clazz.method {
            annotation.name.takeIf { it.isNotBlank() }?.let { name = it.trim() }
            annotation.paramCount.takeIf { it.isNotEmpty() }?.let { paramCount = it.trim().toInt() }
            annotation.param.takeIf { it.isNotEmpty() }?.split(",")
                ?.map { it.trim() }?.let { param(*it.toTypedArray()) }
            annotation.returnType.takeIf { it.isNotEmpty() }?.let { returnType = it.trim() }
            if (annotation.isFindInSuper) superClass(annotation.isOnlySuperClass)
        }

    internal fun Class<*>.findMethodByAnnotation(annotation: ProxyMethod) =
        ReflectTools.findMethodByAnnotation(this, annotation)
}