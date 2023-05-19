package org.liuyi.mifreeformx.bean

import com.highcapable.yukihookapi.hook.xposed.prefs.YukiHookPrefsBridge

/**
 * @Author: Liuyi
 * @Date: 2023/05/12/20:32:26
 * @Description:
 */
interface BlackList {

    fun add(prefs: YukiHookPrefsBridge? = null, item: String)

    fun addAll(prefs: YukiHookPrefsBridge? = null, items: Set<String>)

    fun remove(prefs: YukiHookPrefsBridge? = null, item: String)

    fun contains(prefs: YukiHookPrefsBridge? = null, item: String): Boolean

    fun size(prefs: YukiHookPrefsBridge? = null): Int

    fun clear(prefs: YukiHookPrefsBridge? = null)

    fun getAll(prefs: YukiHookPrefsBridge? = null): Set<String>
}