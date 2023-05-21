package org.liuyi.mifreeformx.activity.page

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextV
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.xposed.hooker.IgnorePopViewHooker


/**
 * @Author: Liuyi
 * @Date: 2023/05/22/0:09:57
 * @Description:
 */
@BMPage(title = "忽略弹窗", key = "IgnorePopViewPage")
class IgnorePopViewPage : MyBasePage() {
    override fun onCreate() {
        TextWithSwitch(
            TextV(text = "二次确认启动弹窗"),
            createCustomSwitchV(
                IgnorePopViewHooker.APP_ACTION_CHECK_ALLOW_START_ACTIVITY,
                IgnorePopViewHooker.IGNORE_POP_VIEW_BY_ACTION
            )
        )
        TextWithSwitch(
            TextV(text = "WIFI故障弹窗"),
            createCustomSwitchV(
                IgnorePopViewHooker.NET_ACTION_PROMPT_UNVALIDATED,
                IgnorePopViewHooker.IGNORE_POP_VIEW_BY_ACTION
            )
        )
        TextWithSwitch(
            TextV(text = "请求权限弹窗"),
            createCustomSwitchV(
                IgnorePopViewHooker.ACTION_REQUEST_PERMISSIONS,
                IgnorePopViewHooker.IGNORE_POP_VIEW_BY_ACTION
            )
        )
    }

    private fun createCustomSwitchV(action: String, data: PrefsData<Set<String>>): SwitchV {
        return SwitchV(
            "ignore_pop_view_page_by_action_$action",
            defValue = activity.prefs().get(data).contains(action)
        ) { isOpen ->
            activity.prefs().run {
                get(data).toMutableSet().apply {
                    if (isOpen) add(action)
                    else remove(action)
                }.let { edit { put(data, it) } }
            }
        }
    }
}