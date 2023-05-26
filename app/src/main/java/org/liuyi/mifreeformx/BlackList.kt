package org.liuyi.mifreeformx

import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.bean.BlackListBean

/**
 * @Author: Liuyi
 * @Date: 2023/05/09/9:47:41
 * @Description:
 */


object BlackList {

    object AppJumpSourceBlacklist :
        BlackListBean(
            PrefsData(
                "app_jump_source_blacklist",
                setOf(
                    "com.android.providers.media.module",
                    "android",
                    "com.android.shell",
                    "com.android.providers.media.module",
                    "com.miui.securitycenter",
                    "com.miui.securityadd",
                    "com.android.systemui",
                    "com.xiaomi.mirror"         // 小米妙享
                )
            )
        )

    object AppJumpTargetBlacklist : BlackListBean(
        PrefsData(
            "app_jump_target_blacklist",
            setOf(
                "com.android.camera",
                "com.miui.tsmclient",
                "com.lbe.security.miui",
                "com.miui.securitycenter",
                "com.miui.personalassistant",
                "com.android.phone"
            )
        )
    )

    object AppShareSourceBlacklist :
        BlackListBean(PrefsData("app_share_source_blacklist", setOf("com.android.providers.media.module")))

    object AppShareTargetBlacklist : BlackListBean(
        PrefsData(
            "app_share_target_blacklist",
            setOf(
                "com.android.camera",
                "com.miui.tsmclient",
                "com.lbe.security.miui",
                "com.miui.mediaeditor"
            )
        )
    )

    object ParallelFreeformWhitelist : BlackListBean(
        PrefsData(
            "parallel_small_window_whitelist",
            setOf()
        )
    )

    object TileBlacklist : BlackListBean(
        PrefsData(
            "tile_blacklist",
            setOf("com.android.camera", "com.miui.tsmclient", "com.lbe.security.miui")
        )
    )
}