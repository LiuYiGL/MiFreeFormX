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

    var forceList: Set<String> = prefsData?.value?: setOf()

    override fun add(prefs: YukiHookPrefsBridge?, item: String) {
        if (item in forceList) {
            loggerD(msg = "$item is forced to open")
            return
        }
        if (prefs != null && prefsData != null) {
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

    override fun remove(prefs: YukiHookPrefsBridge?, item: String) {
        if (item in forceList) {
            loggerD(msg = "$item is forced to open")
            return
        }
        if (prefs != null && prefsData != null) {
            val mutableSet = prefs.get(prefsData).toMutableSet()
            mutableSet.remove(item)
            prefs.edit { put(prefsData, mutableSet) }
        }
    }

    override fun contains(prefs: YukiHookPrefsBridge?, item: String): Boolean {
        return item in forceList || prefsData?.let { prefs?.get(it)?.contains(item) } ?: false
    }

    override fun size(prefs: YukiHookPrefsBridge?, item: String): Int {
        return if (prefs != null && prefsData != null) {
            forceList.toMutableSet().apply { addAll(prefs.get(prefsData)) }.size
        } else forceList.size
    }

    override fun clear(prefs: YukiHookPrefsBridge?, item: String) {
        if (prefs != null && prefsData != null) {
            prefs.edit { put(prefsData, setOf()) }
        }
    }

    override fun getAll(prefs: YukiHookPrefsBridge?): Set<String> {
        return if (prefs != null && prefsData != null) {
            forceList.toMutableSet().apply { addAll(prefs.get(prefsData)) }
        } else forceList
    }

    fun contains(prefs: YukiHookPrefsBridge? = null, intent: Intent, context: Context? = null): Boolean {
        val comparable = intent.component ?: context?.packageManager?.let { intent.resolveActivity(it) }
        return comparable?.let { contains(prefs, it.packageName) } ?: false
    }
}