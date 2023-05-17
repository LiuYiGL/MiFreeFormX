package org.liuyi.mifreeformx.xposed.hooker

import android.app.ActivityOptions
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.proxy.framework.ActivityOptionsInjector
import org.liuyi.mifreeformx.proxy.framework.MiuiFreeFormManagerService
import org.liuyi.mifreeformx.proxy.framework.MiuiMultiWindowUtils
import org.liuyi.mifreeformx.proxy.framework.Task
import org.liuyi.mifreeformx.utils.RectUtils
import org.liuyi.mifreeformx.utils.callMethodByName
import org.liuyi.mifreeformx.utils.getLaunchWindowingModeExt
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/15/15:37:30
 * @Description:
 */
object SizeAndPositionHooker : LyBaseHooker() {

    val CUSTOM_POSITION_CENTER_X = PrefsData("custom_position_center_x", -1)
    val CUSTOM_POSITION_CENTER_Y = PrefsData("custom_position_center_y", -1)
    val CUSTOM_SCALE = PrefsData("custom_scale", 0f)

    val CUSTOM_POSITION_CENTER_HORIZONTAL_X = PrefsData("custom_position_center_horizontal_x", -1)
    val CUSTOM_POSITION_CENTER_HORIZONTAL_Y = PrefsData("custom_position_center_horizontal_y", -1)
    val CUSTOM_HORIZONTAL_SCALE = PrefsData("custom_horizontal_scale", 0f)

    val OPEN_CUSTOM_SIZE_AND_POSITION = PrefsData("open_custom_size_and_position", false)
    val OPEN_CUSTOM_SIZE_AND_POSITION_MODE = PrefsData("open_custom_size_and_position_mode", 0)
    val openCustomSizeAndPositionModeString = listOf("关闭", "仅竖屏开启", "仅横屏开启", "始终开启")


    override fun onHook() {

        /**
         * 自定义小窗大小与位置
         */
        "com.android.server.wm.MiuiFreeFormManagerService".hook {
            injectMember {
                method { name = "onStartActivity" }
                beforeHook {
                    loggerD(msg = "参数: ${args.toList()}")
                    val task = args[0]?.getProxyAs<Task>()!!
                    val options = (args[1] as ActivityOptions?) ?: return@beforeHook
                    val miuiFreeFormManagerService = instance.getProxyAs<MiuiFreeFormManagerService>()
                    val context = miuiFreeFormManagerService.mActivityTaskManagerService?.mContext!!
                    val displaySize = MiuiMultiWindowUtils.StaticProxy.getDisplaySize(context)!!
                    val mode = prefs.get(OPEN_CUSTOM_SIZE_AND_POSITION_MODE)
                    if (
                        prefs.get(OPEN_CUSTOM_SIZE_AND_POSITION_MODE) != 0
                        && !task.isActivityTypeHome()
                        && options.getLaunchWindowingModeExt() == 5
                    ) {
                        val isVertical = displaySize.height() > displaySize.width()
                        if (isVertical && mode == 2) return@beforeHook
                        if (!isVertical && mode == 1) return@beforeHook

                        // 自定义数据
                        var newScale = if (isVertical) prefs.get(CUSTOM_SCALE) else prefs.get(CUSTOM_HORIZONTAL_SCALE)
                        val centerX = if (isVertical) prefs.get(CUSTOM_POSITION_CENTER_X) else prefs.get(
                            CUSTOM_POSITION_CENTER_HORIZONTAL_X
                        )
                        val centerY = if (isVertical) prefs.get(CUSTOM_POSITION_CENTER_Y) else prefs.get(
                            CUSTOM_POSITION_CENTER_HORIZONTAL_Y
                        )

                        // 准备信息
                        val defaultScale = MiuiMultiWindowUtils.StaticProxy.getFreeFormScale(
                            isVertical,
                            false,
                            MiuiMultiWindowUtils.StaticProxy.getScreenType(context)
                        )

                        val launchBounds = options.launchBounds!!
                        val optionsInjector = options.callMethodByName("getActivityOptionsInjector")
                            ?.getProxyAs<ActivityOptionsInjector>()!!
                        var scaleInOptions = optionsInjector.mFreeformScale
                        loggerD(msg = "scaleInOptions: $scaleInOptions, launchBounds: $launchBounds, defaultScale: $defaultScale")

                        if (scaleInOptions == -1.0f) {
                            scaleInOptions = defaultScale
                        }
                        // 只处理默认的大小
                        if (scaleInOptions == defaultScale) {
                            // 缩放
                            newScale = if (newScale != 0f) newScale else scaleInOptions
                            val newRect =
                                if (newScale != 0f)
                                    RectUtils.scaleRectWithCenter(launchBounds, scaleInOptions, newScale)
                                else launchBounds
                            // 移动
                            RectUtils.moveRectWithCenter(newRect, newScale, centerX, centerY)
                            loggerD(msg = "自定义窗口: newScale: $newScale, newRect: $newRect")
                            // 设置
                            options.launchBounds = newRect
                            optionsInjector.mFreeformScale = newScale
                        }
                    }
                }
            }
        }
    }
}