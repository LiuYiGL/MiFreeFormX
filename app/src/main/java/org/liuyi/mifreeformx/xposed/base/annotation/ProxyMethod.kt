package org.liuyi.mifreeformx.xposed.base.annotation

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/6:01:13
 * @Description:
 */
annotation class ProxyMethod(
    val name: String = "",
    val paramCount: String = "",
    val param: String = "",     // 中间用','分开
    val returnType: String = "",
    val isFindInSuper: Boolean = false,
    val isOnlySuperClass: Boolean = false
) {
    companion object {
        fun ProxyMethod.getNameOrNull() = name.takeIf { it.isNotBlank() }?.run { trim() }

        fun ProxyMethod.getParamCountOrNull() = paramCount.takeIf { it.isNotBlank() }?.run { trim().toInt() }

        fun ProxyMethod.getParamOrNull() = param.takeIf { it.isNotBlank() }?.split(",")?.map { it.trim() }

        fun ProxyMethod.getReturnTypeOrNull() = returnType.takeIf { it.isNotBlank() }?.run { trim() }
    }
}
