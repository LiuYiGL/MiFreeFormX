package org.liuyi.mifreeformx.xposed.base

import com.highcapable.yukihookapi.hook.core.finder.members.FieldFinder
import com.highcapable.yukihookapi.hook.core.finder.members.MethodFinder
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import org.liuyi.mifreeformx.xposed.base.ReflectTools.findFieldByAnnotation
import org.liuyi.mifreeformx.xposed.base.ReflectTools.findMethodByAnnotation
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyField
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/4:30:18
 * @Description:
 */
abstract class LyBaseHooker : YukiBaseHooker() {

    val methodMap = HashMap<String, Any?>()

    /**
     * 创建一个代理
     *
     * @param clazz 代理接口类，必须继承 ProxyInterface 接口
     * @param instance 代理类，要代理的实例，如静态则放 Class对象，不允许为null
     * @return 一个代理对象
     */
    fun createProxy(clazz: Class<*>, instance: Any): Any? = Proxy.newProxyInstance(
        clazz.classLoader,
        arrayOf(clazz, ProxyInterface::class.java),
        ReflectProxy(instance)
    )

    inline fun <reified T> getProxy(instance: Any) = createProxy(T::class.java, instance) as T

    inline fun <reified T> Any.getProxyAs() = getProxy<T>(this)

    /**
     * 代理的实例对象
     * @property _instance
     */
    inner class ReflectProxy(private var _instance: Any) : InvocationHandler, ProxyInterface {
        private var isStatic = _instance is Class<*>

        override val clazz = if (_instance is Class<*>) _instance as Class<*> else _instance.javaClass

        override var instance: Any?
            get() = if (isStatic) null else _instance
            set(value) {
                isStatic = value == null
                _instance = value ?: clazz
            }

        /**
         * 将参数转成源对象
         *
         * @param args
         */
        private fun param2OriIfNeed(vararg args: Any?) = args.map {
            if (ProxyInterface::class.java.isInstance(it)) (it as ProxyInterface).instance
            else it
        }.toTypedArray()

        /**
         * 将结果转成代理对象
         *
         * @param res
         * @param resType
         */
        private fun result2ProxyIfNeed(res: Any, resType: Class<*>) =
            if (resType.isInterface && ProxyInterface::class.java.isAssignableFrom(resType)) {
                createProxy(resType, res)
            } else res


        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            if (method == null) return null
            kotlin.runCatching {
                if (method.declaringClass == ProxyInterface::class.java) {
                    // 如果调用 ProxyInterface 的方法应由代理响应
                    return method.invoke(this, *args.orEmpty())
                } else if (method.isAnnotationPresent(ProxyField::class.java)) {
                    val annotation = method.getAnnotation(ProxyField::class.java)!!
                    val uniqueCode = "[$clazz][$annotation]"
                    if (uniqueCode !in methodMap) {
                        // 初始化
                        methodMap[uniqueCode] = clazz.findFieldByAnnotation(annotation)
                    }

                    return (methodMap[uniqueCode] as FieldFinder.Result).get(instance).run {
                        if (args == null) any() // 取值操作
                        else set(param2OriIfNeed(*args)[0])// 赋值操作
                    }?.let { result2ProxyIfNeed(it, method.returnType) }

                } else if (method.isAnnotationPresent(ProxyMethod::class.java)) {
                    val annotation = method.getAnnotation(ProxyMethod::class.java)!!
                    val uniqueCode = "[$clazz][$annotation]"
                    if (uniqueCode !in methodMap) {
                        methodMap[uniqueCode] = clazz.findMethodByAnnotation(annotation)
                    }
                    return (methodMap[uniqueCode] as MethodFinder.Result).get(instance)
                        .call(*param2OriIfNeed(*args.orEmpty()))   // 参数转换
                        ?.let { result2ProxyIfNeed(it, method.returnType) }     // 结果转换

                } else {
                    return method.invoke(_instance, *args.orEmpty())
                }
            }.exceptionOrNull()?.let { loggerE(e = it) }
            return null
        }
    }
}