package org.liuyi.mifreeformx.xposed.base.annotation

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/6:00:54
 * @Description:
 */
annotation class ProxyField(
    val name: String = "",
    val type: String = "",
    val isFindInSuper: Boolean = false,
    val isOnlySuperClass: Boolean = false,
) {
    companion object {
        fun ProxyField.getNameOrNull() = name.takeIf { it.isNotBlank() }?.run { trim() }

        fun ProxyField.getTypeOrNull() = type.takeIf { it.isNotBlank() }?.run { trim() }
    }
}
