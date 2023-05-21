package org.liuyi.mifreeformx.xposed.hooker

import android.app.ActivityOptions
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.proxy.framework.ActivityStarter
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/22/0:17:48
 * @Description:
 */
object IgnorePopViewHooker : LyBaseHooker() {

    const val APP_ACTION_CHECK_ALLOW_START_ACTIVITY = "android.app.action.CHECK_ALLOW_START_ACTIVITY"   // 二次确认
    const val NET_ACTION_PROMPT_UNVALIDATED = "android.net.action.PROMPT_UNVALIDATED"                   // wifi 不可用
    const val ACTION_REQUEST_PERMISSIONS = "android.content.pm.action.REQUEST_PERMISSIONS"              // 权限弹窗

    val IGNORE_POP_VIEW_BY_ACTION = PrefsData(
        "ignore_pop_view_by_action",
        setOf(
            APP_ACTION_CHECK_ALLOW_START_ACTIVITY,
            NET_ACTION_PROMPT_UNVALIDATED,
        )
    )

    override fun onHook() {

        "com.android.server.wm.ActivityStarter\$Request".hook {
            injectMember {
                method { name = "resolveActivity" }
                afterHook {
                    val actionSet = prefs.get(IGNORE_POP_VIEW_BY_ACTION)
                    if (actionSet.isEmpty()) return@afterHook
                    val request = instance.getProxyAs<ActivityStarter.Request>()
                    logD("intent: ${request.intent}")
                    request.intent?.action?.let {
                        if (actionSet.contains(it)) {
                            request.activityOptions?.mOriginalOptions = ActivityOptions.makeBasic()
                        }
                    }
                }
            }
        }
    }
}