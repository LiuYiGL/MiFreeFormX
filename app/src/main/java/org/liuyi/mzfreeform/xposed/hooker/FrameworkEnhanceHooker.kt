package org.liuyi.mzfreeform.xposed.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ActivityThreadClass
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
                    by(this, DataConst.CANCEL_MULTI_WINDOW_LIMIT) {
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
    }
}

