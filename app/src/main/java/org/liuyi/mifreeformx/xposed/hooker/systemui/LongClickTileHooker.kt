package org.liuyi.mifreeformx.xposed.hooker.systemui

import android.content.Intent
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.bean.BlackListBean
import org.liuyi.mifreeformx.intent.LyIntent
import org.liuyi.mifreeformx.proxy.systemui.CentralSurfacesImpl
import org.liuyi.mifreeformx.proxy.systemui.CommonUtil
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/24/1:42:37
 * @Description:
 */
object LongClickTileHooker : LyBaseHooker() {

    val OPEN_MODE = PrefsData("long_click_tile_open_mode", 0)
    val OPEN_MODE_TEXT = listOf("默认", "预设", "强制全部")

    object BlackList : BlackListBean(
        PrefsData(
            "tile_blacklist",
            setOf("com.android.camera", "com.miui.tsmclient", "com.lbe.security.miui")
        )
    )


    override fun onHook() {
        /**
         * Hook com.android.systemui.statusbar.phone.CentralSurfacesImpl#startActivityDismissingKeyguard
         * (android.content.Intent, boolean, boolean, boolean, com.android.systemui.plugins.ActivityStarter.Callback,
         * int, com.android.systemui.animation.ActivityLaunchAnimator.Controller, android.os.UserHandle)
         *
         * 当 长按 Tile 时，或点击设置时触发方法，可在此进行判断
         */
        "com.android.systemui.statusbar.phone.CentralSurfacesImpl".hook {
            injectMember {
                method {
                    name("startActivityDismissingKeyguard")
                    paramCount(8)
                }
                beforeHook {
                    val mode = prefs.get(OPEN_MODE)
                    if (mode == 0) return@beforeHook
                    logD("模式：${OPEN_MODE_TEXT[mode]} 参数：${args.toList()}")

                    var intent = args[0] as Intent?
                    val context = instance.getProxyAs<CentralSurfacesImpl>().mContext
                    if (intent != null && context != null) {
                        // 备份Intent，防止SB的系统用同一个实例吃遍天下
                        intent = Intent(intent)
                        args[0] = intent
                        val topActivity = CommonUtil.StaticProxy.getTopActivity()
                        val componentName = intent.resolveActivity(context.packageManager)
                        logD("顶部：$topActivity, 当前：$componentName")
                        // 如果是顶部App 则不处理
                        if (topActivity?.packageName == componentName.packageName) return@beforeHook
                        when (mode) {
                            1 -> if (isTile(intent) && !BlackList.contains(prefs, componentName.packageName)) {
                                args[5] = getFreeFormFlag(args[5] as Int)
                            }

                            2 -> args[5] = getFreeFormFlag(args[5] as Int)
                        }
                    }
                }
            }
        }
    }

    private fun getFreeFormFlag(sourceFlag: Int = 0): Int {
        return sourceFlag or
                LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    }

    /**
     * 通过Intent 判断是否需要使用小窗打开
     *
     * @param intent
     * @return
     */
    private fun isTile(intent: Intent?): Boolean {
        return intent?.run {
            action?.let {
                if (it == "android.service.quicksettings.action.QS_TILE_PREFERENCES") true
                else if (it == "android.settings.SETTINGS") false
                else if (it.startsWith("android.settings")) true
                else null
            } ?: component?.let {
                if (it.packageName == "com.android.settings" || it.packageName == "com.android.phone") true
                else null
            } ?: `package`?.let {
                if (it.startsWith("com.android")) true
                else null
            }
        } ?: false
    }
}