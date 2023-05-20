package org.liuyi.mifreeformx

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
     * 分享至应用
     */
    val SHARE_TO_APP = PrefsData("share_to_app", false)

    /**
     * 分享时强制使用新窗口打开
     */
    val SHARE_TO_APP_FORCE_NEW_TASK = PrefsData("share_to_app_force_new_task", false)

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
    val LIFT_WINDOW_NUM_LIMIT = PrefsData("cancel_multi_window_limit", false)

    /**
     * 修复小窗启动应用确认 修复打开小窗应用触发二次确认时会缩小当前应用的Bug
     */
    val FIX_START_SMALL_WINDOW_CONFIRM = PrefsData("fix_start_small_window_confirm", false)

    /**
     * 平行小窗Plus，实现边刷文章，边回消息
     */
    val PARALLEL_MULTI_WINDOW_PLUS = PrefsData("parallel_multi_window_plus", false)

    /**
     * 当小窗失去焦点时会执行一些操作
     * 0 - 默认
     * 1 - 挂起小窗
     * 2 - 贴边小窗
     * 3 - 关闭小窗
     */
    val FREEFORM_LOSE_FOCUS_OPT_TYPE = PrefsData("freeform_lose_focus_opt_type", 0)
    val freeformLoseFocusOptTypeString = listOf("默认", "小窗挂起", "小窗贴边", "小窗关闭")

    val DETAILED_LOG = PrefsData("detailed_log", false)
}