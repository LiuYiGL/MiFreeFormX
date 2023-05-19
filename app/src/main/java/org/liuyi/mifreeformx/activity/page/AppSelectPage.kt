package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.SpinnerV
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
        var preList: BlackListBean? = null
    }

    private var currentList: BlackListBean? = null


    private val mAppSelectAdapter by lazy {
        AppSelectAdapter(activity, allAppInfo.toMutableList(), mutableSetOf(), setOf()) { _, _ ->
            currentList?.addAll(activity.prefs(), selectedSet)
        }
    }


    override fun onCreate() {
        val allAppInfoListBak = mAppSelectAdapter.mAppInfoList
        var filterAppInfoListBak = mAppSelectAdapter.mAppInfoList
        EditTextWithSpinner(
            "", "输入软件名、包名", editTextWeight = 1.5f,
            spinnerV = SpinnerV("全部") {
                add("全部") {
                    mAppSelectAdapter.mAppInfoList = allAppInfoListBak.sort(mAppSelectAdapter.selectedSet)
                    filterAppInfoListBak = mAppSelectAdapter.mAppInfoList
                    mAppSelectAdapter.notifyDataSetChanged()
                }
                add("用户应用") {
                    mAppSelectAdapter.mAppInfoList =
                        allAppInfoListBak.filter { appInfo -> !appInfo.isSystem }.toMutableList()
                            .sort(mAppSelectAdapter.selectedSet)
                    filterAppInfoListBak = mAppSelectAdapter.mAppInfoList
                    mAppSelectAdapter.notifyDataSetChanged()
                }
                add("系统应用") {
                    mAppSelectAdapter.mAppInfoList =
                        allAppInfoListBak.filter { appInfo -> appInfo.isSystem }.toMutableList()
                            .sort(mAppSelectAdapter.selectedSet)
                    filterAppInfoListBak = mAppSelectAdapter.mAppInfoList
                    mAppSelectAdapter.notifyDataSetChanged()
                }
            }
        ) {
            mAppSelectAdapter.mAppInfoList =
                if (it.isBlank()) filterAppInfoListBak
                else filterAppInfoListBak.filterByInputStr(it)
            mAppSelectAdapter.mAppInfoList.sort(mAppSelectAdapter.selectedSet)
            mAppSelectAdapter.notifyDataSetChanged()
        }

        TextA("恢复默认设置", onClickListener = {
            currentList?.clear(activity.prefs())
            mAppSelectAdapter.refreshList()
        })

        List {
            adapter = mAppSelectAdapter
        }
    }

    override fun asyncInit(fragment: MIUIFragment) {
        fragment.showLoading()
        // 初始化
        currentList = preList
        mAppSelectAdapter.refreshList()
        preList = null
        fragment.initData()
        fragment.closeLoading()
    }

    fun MutableList<AppUtils.AppInfo>.sort(selected: Set<String>): MutableList<AppUtils.AppInfo> {
        sortWith { a1, a2 ->
            if (selected.contains(a1.packageName) && selected.contains(a2.packageName)) {
                PinyinUtils.ccs2Pinyin(a1.name).compareTo(PinyinUtils.ccs2Pinyin(a2.name))
            } else if (selected.contains(a1.packageName)) {
                -1
            } else if (selected.contains(a2.packageName)) {
                1
            } else PinyinUtils.ccs2Pinyin(a1.name).compareTo(PinyinUtils.ccs2Pinyin(a2.name))
        }
        return this
    }

    fun MutableList<AppUtils.AppInfo>.filterByInputStr(str: String): MutableList<AppUtils.AppInfo> {
        return filter {
            it.name.contains(str, true)
                    || it.packageName.contains(str, true)
        }.toMutableList()
    }

    fun AppSelectAdapter.refreshList(list: BlackListBean? = currentList) {
        val current = list?.getAll(activity.prefs())

        mAppInfoList = current?.let {
            allAppInfo.toMutableList().sort(current)
        } ?: mutableListOf()

        selectedSet = current?.toMutableSet() ?: mutableSetOf()

        loggerD(msg = "当前黑名单: $currentList")
        loggerD(msg = "当前选择项: $selectedSet")
        notifyDataSetChanged()
    }
}
