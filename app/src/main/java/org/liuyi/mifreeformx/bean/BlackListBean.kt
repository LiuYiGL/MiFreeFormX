package org.liuyi.mifreeformx.bean

import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.xposed.prefs.YukiHookPrefsBridge
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData

/**
 * @Author: Liuyi
 * @Date: 2023/05/12/20:31:43
 * @Description:
 */
open class BlackListBean(private val prefsData: PrefsData<Set<String>>? = null) : BlackList {


    override fun add(prefs: YukiHookPrefsBridge?, item: String?) {
        if (prefs != null && prefsData != null && item != null) {
            val mutableSet = prefs.get(prefsData).toMutableSet()
            mutableSet.add(item)
            prefs.edit { put(prefsData, mutableSet) }
        }
    }

    override fun addAll(prefs: YukiHookPrefsBridge?, items: Set<String>) {
        loggerD(msg = "$prefs $items $prefsData")
        if (prefs != null && prefsData != null) {
            prefs.edit { put(prefsData, items) }
        }
    }

    override fun remove(prefs: YukiHookPrefsBridge?, item: String?) {
        if (prefs != null && prefsData != null) {
            val mutableSet = prefs.get(prefsData).toMutableSet()
            mutableSet.remove(item)
            prefs.edit { put(prefsData, mutableSet) }
        }
    }

    override fun contains(prefs: YukiHookPrefsBridge?, item: String?): Boolean {
        item ?: return false
        return prefsData?.let { prefs?.get(it)?.contains(item) } ?: false
    }

    override fun size(prefs: YukiHookPrefsBridge?): Int {
        return if (prefs != null && prefsData != null) {
            prefs.get(prefsData).size
        } else 0
    }

    override fun clear(prefs: YukiHookPrefsBridge?) {
        if (prefs != null && prefsData != null) {
            prefs.edit { remove(prefsData) }
        }
    }

    override fun getAll(prefs: YukiHookPrefsBridge?): Set<String> {
        return if (prefs != null && prefsData != null) {
            prefs.get(prefsData)
        } else setOf()
    }

    fun contains(prefs: YukiHookPrefsBridge? = null, intent: Intent, context: Context? = null): Boolean {
        val comparable = intent.component ?: context?.packageManager?.let { intent.resolveActivity(it) }
        return comparable?.let { contains(prefs, it.packageName) } ?: false
    }
}