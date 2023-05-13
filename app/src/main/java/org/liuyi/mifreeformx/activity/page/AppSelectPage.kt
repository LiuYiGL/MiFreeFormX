package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.fragment.MIUIFragment
import com.blankj.utilcode.util.AppUtils
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.log.loggerD
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.adapter.AppSelectAdapter
import org.liuyi.mifreeformx.bean.BlackListBean
import org.liuyi.mifreeformx.utils.PinyinUtils
import kotlin.concurrent.thread

/**
 * @Author: Liuyi
 * @Date: 2023/05/07/13:26:35
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(key = "AppSelectPage", titleId = R.string.select_app)
class AppSelectPage : MyBasePage() {

    init {
        skipLoadItem = true
    }

    companion object {
        init {
            thread { allAppInfo }
        }

        val allAppInfo: MutableList<AppUtils.AppInfo> by lazy { AppUtils.getAppsInfo() }
        var currentBlackList: BlackListBean? = null
    }

    private var lastBlackList: BlackListBean? = null


    private val mAppSelectAdapter by lazy {
        AppSelectAdapter(activity, allAppInfo.toMutableList(), mutableSetOf(), setOf()) { _, _ ->
            lastBlackList?.addAll(activity.prefs(), selectedSet)
        }
    }


    override fun onCreate() {
        List {
            adapter = mAppSelectAdapter
        }
    }

    override fun asyncInit(fragment: MIUIFragment) {
        fragment.showLoading()
        // 初始化
        mAppSelectAdapter.apply {
            val current = currentBlackList?.getAll(activity.prefs())

            mAppInfoList = current?.let {
                allAppInfo.toMutableList().apply {
                    sortWith { a1, a2 ->
                        if (current.contains(a1.packageName) && current.contains(a2.packageName)) {
                            PinyinUtils.ccs2Pinyin(a1.name).compareTo(PinyinUtils.ccs2Pinyin(a2.name))
                        } else if (current.contains(a1.packageName)) {
                            -1
                        } else if (current.contains(a2.packageName)) {
                            1
                        } else PinyinUtils.ccs2Pinyin(a1.name).compareTo(PinyinUtils.ccs2Pinyin(a2.name))
                    }
                }
            } ?: mutableListOf()

            disEnabledSet = currentBlackList?.forceList ?: setOf()
            selectedSet = current?.toMutableSet() ?: mutableSetOf()

            loggerD(msg = "当前黑名单: $currentBlackList")
            loggerD(msg = "上次黑名单: $lastBlackList")
            loggerD(msg = "不可选项: $disEnabledSet")
            loggerD(msg = "当前选择项: $selectedSet")
            notifyDataSetChanged()
        }
        lastBlackList = currentBlackList
        currentBlackList = null
        fragment.initData()
        fragment.closeLoading()
    }
}
