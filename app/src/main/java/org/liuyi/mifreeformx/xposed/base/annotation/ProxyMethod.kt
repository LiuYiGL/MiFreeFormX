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
)
