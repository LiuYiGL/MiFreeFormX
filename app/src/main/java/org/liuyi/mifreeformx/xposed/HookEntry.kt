package org.liuyi.mifreeformx.xposed

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.log.loggerI
import com.highcapable.yukihookapi.hook.log.loggerW
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.xposed.hooker.*
import org.liuyi.mifreeformx.xposed.hooker.SystemUiHooker
import org.liuyi.mifreeformx.xposed.hooker.systemui.ClickNotificationHooker
import org.liuyi.mifreeformx.xposed.hooker.systemui.LongClickTileHooker

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/17:12:07
 * @Description:
 */
@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {

    override fun onInit() {
        configs {
            debugLog {
                tag = "MzFreeForm"
            }
            isEnableHookSharedPreferences = true
        }
    }

    override fun onHook() = YukiHookAPI.encase {
        if (!prefs.isPreferencesAvailable) {
            loggerW(msg = "模块配置获取失败")
        } else if (!prefs.get(DataConst.MAIN_SWITCH)) {
            loggerW(msg = "模块已关闭")
        } else {
            YukiHookAPI.Configs.isDebug = prefs.get(DataConst.DETAILED_LOG)
            loadApp {
                dataChannel.wait<Boolean>(DataConst.DETAILED_LOG.key) {
                    YukiHookAPI.Configs.isDebug = it
                }
            }
            loggerI(msg = "Starting Hook！！")
            loadSystem(
                FrameworkBaseHooker,
                FrameworkEnhanceHooker,
                SizeAndPositionHooker,
                FreeformOutsideMotionHooker,
                IgnorePopViewHooker,
                ParallelSmallWindowHooker
            )
            loadApp("com.android.systemui", SystemUiHooker, ClickNotificationHooker, LongClickTileHooker)
        }
    }

    override fun onXposedEvent() {

    }
}