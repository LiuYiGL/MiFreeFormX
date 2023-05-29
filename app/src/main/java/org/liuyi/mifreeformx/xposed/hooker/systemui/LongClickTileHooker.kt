package org.liuyi.mifreeformx.xposed.hooker.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.bean.BlackListBean
import org.liuyi.mifreeformx.intent.LyIntent
import org.liuyi.mifreeformx.proxy.systemui.CommonUtil
import org.liuyi.mifreeformx.utils.callMethodByName
import org.liuyi.mifreeformx.utils.containsFlag
import org.liuyi.mifreeformx.utils.getFieldValueOrNull
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/24/1:42:37
 * @Description: 逻辑：先通过【ControlCenterActivityStarter#postStartActivityDismissingKeyguard】在Intent中添加自定义标志，
 * 由于版本差异。在不同的版本加载不同的Hooker，判断只要存在该标志，则添加小窗标志
 */
object LongClickTileHooker : LyBaseHooker() {

    val OPEN_MODE = PrefsData("long_click_tile_open_mode", 0)
    val OPEN_MODE_TEXT = listOf("关闭", "开启")

    /**
     * 长按Tile 小窗打开
     */
    val LONG_PRESS_TILE = PrefsData("long_press_tile", false)

    object BlackList : BlackListBean(
        PrefsData(
            "tile_blacklist",
            setOf("com.android.camera", "com.miui.tsmclient", "com.lbe.security.miui")
        )
    )

    @SuppressLint("WrongConstant")
    override fun onHook() {

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> loadHooker(HookerForA13)
            else -> loadHooker(HookerForA12)
        }

        /**
         * 当 长按 Tile 时，可在此进行判断
         * Hook com.android.systemui.controlcenter.policy.ControlCenterActivityStarter#postStartActivityDismissingKeyguard
         * (android.content.Intent, int, com.android.systemui.animation.ActivityLaunchAnimator.Controller)
         *
         * 参考：com.android.systemui.qs.tileimpl.QSTileImpl#handleLongClick
         */
        var context: Context? = null
        "com.android.systemui.controlcenter.policy.ControlCenterActivityStarter".hook {
            injectMember {
                method {
                    name = "postStartActivityDismissingKeyguard"
                    paramCount = 3  // Intent intent, int i, ActivityLaunchAnimator.Controller controller
                }
                beforeHook {
                    if (!prefs.get(LONG_PRESS_TILE)) return@beforeHook
                    logD("参数：${args.toList()}")
                    if (args[0] != null) {
                        val intent = args[0] as Intent

                        context = context ?: getContextByControlCenterActivityStarter(instance)
                        context?.let {
                            val topActivity = CommonUtil.StaticProxy.getTopActivity()
                            val componentName = intent.resolveActivity(it.packageManager)
                            logD("顶部：$topActivity, 当前：$componentName")
                            // 如果是顶部App 则不处理
                            if (topActivity?.packageName == componentName.packageName) return@beforeHook
                            if (BlackList.contains(prefs, componentName.packageName)) return@beforeHook
                        }
                        intent.addFlags(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM)
                    }
                }
            }
        }
    }

    private object HookerForA13 : LyBaseHooker() {
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
                        if (!prefs.get(LONG_PRESS_TILE)) return@beforeHook
                        logD("参数：${args.toList()}")

                        val intent = args[0] as Intent?
                        if (intent?.containsFlag(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM) == true) {
                            args[5] = getFreeFormFlag(args[5] as Int)
                        }
                    }
                }
            }
        }
    }

    private object HookerForA12 : LyBaseHooker() {
        override fun onHook() {
            /**
             * Hook com.android.systemui.statusbar.phone.CentralSurfacesImpl#startActivityDismissingKeyguard
             * (android.content.Intent, boolean, boolean, boolean, com.android.systemui.plugins.ActivityStarter.Callback,
             * int, com.android.systemui.animation.ActivityLaunchAnimator.Controller, android.os.UserHandle)
             *
             * 当 长按 Tile 时，或点击设置时触发方法，可在此进行判断
             */
            "com.android.systemui.statusbar.phone.StatusBar".hook {
                injectMember {
                    method {
                        name("startActivityDismissingKeyguard")
                        paramCount(7)
                    }
                    beforeHook {
                        if (!prefs.get(LONG_PRESS_TILE)) return@beforeHook
                        logD("参数：${args.toList()}")

                        val intent = args[0] as Intent?
                        if (intent?.containsFlag(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM) == true) {
                            args[5] = getFreeFormFlag(args[5] as Int)
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Context
     *
     * @param instance
     * @return
     */
    private fun getContextByControlCenterActivityStarter(instance: Any): Context? {
        return instance.getFieldValueOrNull("controlCenterController")
            ?.callMethodByName("get")?.getFieldValueOrNull("context") as Context?
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