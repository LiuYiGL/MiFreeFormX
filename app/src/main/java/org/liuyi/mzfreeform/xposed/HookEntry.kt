package org.liuyi.mzfreeform.xposed

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.log.loggerI
import com.highcapable.yukihookapi.hook.log.loggerW
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import org.liuyi.mzfreeform.BuildConfig
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.xposed.hooker.*

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/17:12:07
 * @Description:
 */
@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {

    override fun onInit() {
        configs {
            isDebug = BuildConfig.DEBUG
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
            loggerI(msg = "Starting Hook！！")
            loadSystem(FrameworkEnhanceHooker)
            loadSystem(FrameworkBaseHooker)
            loadApp("com.android.systemui", SystemUiHooker)
        }
    }

    override fun onXposedEvent() {

    }
}