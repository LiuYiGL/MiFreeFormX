package org.liuyi.mifreeformx.xposed.hooker.android

import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.bean.BlackListBean
import org.liuyi.mifreeformx.proxy.framework.MiuiFreeFormActivityStack
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/25/2:52:45
 * @Description:
 */
object PinnedWinRunHooker : LyBaseHooker() {

    val OPEN_MODE = PrefsData("pinned_win_always_run_mode", 0)
    val OPEN_MODE_TEXT = listOf("关闭", "始终开启", "白名单模式", "黑名单模式")

    val SELECTED_APPS_PREFS_DATE = PrefsData("pinned_win_always_run_selected_apps", setOf<String>())

    object SelectedAppsList : BlackListBean(SELECTED_APPS_PREFS_DATE)

    override fun onHook() {

        /**
         * 贴边时禁止停止运行
         */
        "com.android.server.wm.MiuiFreeFormGestureController".hook {
            injectMember {
                method { name = "moveTaskToBack" }
                beforeHook {
                    val mode = prefs.get(OPEN_MODE)
                    if (mode == 0) return@beforeHook
                    logD("模式：[${OPEN_MODE_TEXT[mode]}] | 参数：${args.toList()}")
                    val stack = args[0]?.getProxyAs<MiuiFreeFormActivityStack>()
                    stack?.getStackPackageName()?.let {
                        when (mode) {
                            1 -> resultNull()
                            2 -> if (SelectedAppsList.contains(prefs, it)) resultNull()
                            3 -> if (!SelectedAppsList.contains(prefs, it)) resultNull()
                        }
                    }
                }
            }
        }
    }
}