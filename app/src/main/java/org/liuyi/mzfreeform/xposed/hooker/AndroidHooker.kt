package org.liuyi.mzfreeform.xposed.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import org.liuyi.mzfreeform.DataConst

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/20:48:04
 * @Description:
 */
object AndroidHooker : YukiBaseHooker() {
    override fun onHook() {
        val clazz = "com.android.server.wm.ActivityTaskManagerService".toClass()
        "com.android.server.wm.ActivityTaskManagerService".hook {
            injectMember(tag = "MzFreeForm") {
                method { name = "startActivity" }
                beforeHook {
                    loggerD(msg = "${this.args.iterator().asSequence().toList()}")
                }
            }
        }

        /**
         * 去除小窗黑名单
         */
        "android.util.MiuiMultiWindowAdapter".hook {
            injectMember {
                method { name("getFreeformBlackList") }
                afterHook {
                    (result as MutableList<*>).apply { clear() }
                }
            }
            injectMember {
                method { name("getFreeformBlackListFromCloud") }
                afterHook { (result as MutableList<*>).apply { clear() } }
            }
        }.by {
            prefs.get(DataConst.DISABLE_FREEFORM_BLACKLIST) || prefs.get(DataConst.LONG_PRESS_TILE)
        }


        "com.android.server.wm.Task".hook {
            injectMember {
                method { name("isResizeable") }
                replaceToTrue()
            }
        }.by { prefs.get(DataConst.FORCE_ACTIVITY_RESIZEABLE) }

        "android.util.MiuiMultiWindowUtils".hook {
            injectMember {
                method { name("supportFreeform") }
                replaceToTrue()
            }
        }.by { prefs.get(DataConst.FORCE_ACTIVITY_RESIZEABLE) }
    }

}