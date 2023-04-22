package org.liuyi.mzfreeform

import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/17:26:32
 * @Description:
 */
object DataConst {
    /**
     * 总开关
     */
    val MAIN_SWITCH = PrefsData("main_switch", false)

    /**
     * 单击通知小窗打开
     */
    val OPEN_NOTICE = PrefsData("open_notice", false)

    /**
     * 长按Tile 小窗打开
     */
    val LONG_PRESS_TILE = PrefsData("long_press_tile", false)

    /**
     * 控制中心强制全部打开
     */
    val FORCE_CONTROL_ALL_OPEN = PrefsData("force_control_all_open", false)

    /**
     * 解除小窗展开通知限制
     */
    val NOTIFY_LIMIT_REMOVE_SMALL_WINDOW = PrefsData("notify_limit_remove_small_window", false)

    /**
     * 应用间跳转
     */
    val APP_JUMP = PrefsData("app_jump", false)

    /**
     * 禁用小窗黑名单
     */
    val DISABLE_FREEFORM_BLACKLIST = PrefsData("disable_freeform_blacklist", false)

    /**
     * 强制所有活动设为可以调整大小
     */
    val FORCE_ACTIVITY_RESIZEABLE = PrefsData("force_activity_resizeable", false)

    /**
     * 禁用多小窗间的位置偏移
     */
    val DISABLE_MULTI_OFFSET = PrefsData("DISABLE_multi_offset", false)

    // 解除小窗数量限制
    val CANCEL_MULTI_WINDOW_LIMIT = PrefsData("cancel_multi_window_limit", false)
}