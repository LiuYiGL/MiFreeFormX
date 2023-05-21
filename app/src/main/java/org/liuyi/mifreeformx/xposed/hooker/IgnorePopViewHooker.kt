package org.liuyi.mifreeformx.xposed.hooker

import android.app.ActivityOptions
import android.content.Context
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.intent.LyIntent
import org.liuyi.mifreeformx.proxy.framework.ActivityStarter
import org.liuyi.mifreeformx.proxy.framework.ActivityTaskSupervisor
import org.liuyi.mifreeformx.proxy.framework.MiuiMultiWindowUtils
import org.liuyi.mifreeformx.proxy.framework.SafeActivityOptions
import org.liuyi.mifreeformx.utils.containsFlag
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
    const val ACTION_XSPACE_RESOLVER_ACTIVITY_FROM_CORE =
        "miui.intent.action.ACTION_XSPACE_RESOLVER_ACTIVITY_FROM_CORE"                                  // 应用分身弹窗

    val IGNORE_POP_VIEW_BY_ACTION = PrefsData(
        "ignore_pop_view_by_action",
        setOf(
            APP_ACTION_CHECK_ALLOW_START_ACTIVITY,
            NET_ACTION_PROMPT_UNVALIDATED,
            ACTION_XSPACE_RESOLVER_ACTIVITY_FROM_CORE
        )
    )
    private val mLaunchWindowingMode by lazy { ActivityOptions::class.java.field { name = "mLaunchWindowingMode" } }

    override fun onHook() {

        "com.android.server.wm.ActivityStarter\$Request".hook {
            injectMember {
                method { name = "resolveActivity" }
                afterHook {
                    val request = instance.getProxyAs<ActivityStarter.Request>()
                    val intent = request.intent ?: return@afterHook
                    logD("参数: ${args.toList()}")
                    logD("intent: $intent")
                    intent.action?.let {
                        if (prefs.get(IGNORE_POP_VIEW_BY_ACTION).contains(it)) {
                            logD("判断为禁止使用的弹窗Action：$it")
                            request.activityOptions?.mOriginalOptions = ActivityOptions.makeBasic()
                            return@afterHook
                        }
                    }
                    if (intent.containsFlag(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM)) {
                        val activityTaskSupervisor = args[0]?.getProxyAs<ActivityTaskSupervisor>() ?: return@afterHook
                        val context = activityTaskSupervisor.mService?.mContext ?: return@afterHook
                        val safeActivityOptions = request.activityOptions
                        logD("使用小窗打开: $safeActivityOptions")
                        request.activityOptions = (safeActivityOptions ?: SafeActivityOptions.StaticProxy.fromBundle(
                            ActivityOptions.makeBasic().toBundle()
                        ))!!.apply {
                            mOriginalOptions = (mOriginalOptions ?: ActivityOptions.makeBasic()).toFreeformMode(context)
                        }
                    }
                }
            }
        }
    }


    private fun ActivityOptions.toFreeformMode(context: Context): ActivityOptions {
        launchBounds = MiuiMultiWindowUtils.StaticProxy.getFreeformRect(context)
        mLaunchWindowingMode.get(this).set(5)
        return this
    }
}