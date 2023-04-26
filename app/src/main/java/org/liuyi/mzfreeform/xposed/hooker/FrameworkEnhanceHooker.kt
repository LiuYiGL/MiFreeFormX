package org.liuyi.mzfreeform.xposed.hooker

import android.content.Intent
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.utils.by
import org.liuyi.mzfreeform.utils.byAny

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/20:48:04
 * @Description:
 */
object FrameworkEnhanceHooker : YukiBaseHooker() {

    override fun onHook() {

        /**
         * 去除小窗黑名单
         */
        "android.util.MiuiMultiWindowAdapter".hook {
            injectMember {
                method { name("getFreeformBlackList") }
                afterHook {
                    byAny(
                        this, DataConst.DISABLE_FREEFORM_BLACKLIST, DataConst.LONG_PRESS_TILE
                    ) {
                        (result as MutableList<*>).apply { clear() }
                    }
                }
            }
            injectMember {
                method { name("getFreeformBlackListFromCloud") }
                afterHook {
                    byAny(
                        this, DataConst.DISABLE_FREEFORM_BLACKLIST, DataConst.LONG_PRESS_TILE
                    ) {
                        (result as MutableList<*>).apply { clear() }
                    }
                }
            }
        }


        "com.android.server.wm.Task".hook {
            injectMember {
                method { name("isResizeable") }
                beforeHook {
                    by(this, DataConst.FORCE_ACTIVITY_RESIZEABLE) {
                        resultTrue()
                    }
                }
            }.by { prefs.get(DataConst.FORCE_ACTIVITY_RESIZEABLE) }
        }

        "android.util.MiuiMultiWindowUtils".hook {
            injectMember {
                method { name("supportFreeform") }
                beforeHook {
                    by(this, DataConst.FORCE_ACTIVITY_RESIZEABLE) {
                        resultTrue()
                    }
                }
            }
        }

        // 解除 小窗应用数量控制
        "com.android.server.wm.MiuiFreeFormStackDisplayStrategy".hook {
            injectMember {
                method { name("getMaxMiuiFreeFormStackCount") }
                beforeHook {
                    by(this, DataConst.LIFT_WINDOW_NUM_LIMIT) {
                        result = 128
                    }
                }
            }
        }

        // 禁用小窗位置偏移 android.util.MiuiMultiWindowUtils#avoidIfNeeded
        "android.util.MiuiMultiWindowUtils".hook {
            injectMember {
                method { name("avoidIfNeeded") }
                beforeHook {
                    by(this, DataConst.DISABLE_MULTI_OFFSET) {
                        resultNull()
                    }
                }
            }
        }

        // 修复打开小窗应用触发二次确认时会缩小当前应用的Bug miui.security.SecurityManager#buildStartIntent
        // Bug 普遍存在与应用间跳转
        "miui.security.SecurityManager".hook {
            // buildStartIntent miui 会执行改方法构建 手机管家的二次确认intent
            injectMember {
                method { name("buildStartIntent") }
                afterHook {
                    byAny(this, DataConst.MAIN_SWITCH) {
                        result<Intent>()?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            }
        }

    }
}

