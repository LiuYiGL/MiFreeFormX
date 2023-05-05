package org.liuyi.mifreeformx.xposed.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.proxy.*
import org.liuyi.mifreeformx.utils.callMethodByName
import org.liuyi.mifreeformx.utils.getFieldValueOrNull

/**
 * @Author: Liuyi
 * @Date: 2023/05/02/4:23:40
 * @Description: 小窗失去焦点时行为
 */
object FreeformLoseFocusHooker : YukiBaseHooker() {
    override fun onHook() {

        /**
         * 任务失去焦点后会执行的方法，Hook这里并判断是否是小窗，再对小窗进行处理
         * com.android.server.wm.Task#onAppFocusChanged
         */
        "com.android.server.wm.Task".hook {
            injectMember {
                method { name("onAppFocusChanged") }
                afterHook {
                    loggerD(msg = "${args.asList()} $instance")
                    // 判断task状态
                    if (args[0] == true) {
                        val taskProxy = TaskProxy(instance)
                        /**
                         * 排除在桌面上的事件，
                         * 可以修复最近任务无法打开小窗，因为最近任务打开小窗后桌面会申请聚焦
                         * 单例通知时回缩界面导致桌面聚焦
                         */
                        if (taskProxy.getActivityType() == 2) return@afterHook

                        val mWmService = taskProxy.mWmService
                        val stackProxy = taskProxy.mAtmService
                            ?.getFieldValueOrNull("mMiuiFreeFormManagerService")
                            ?.callMethodByName("getTopFreeFormActivityStack")
                            ?.let { MiuiFreeFormActivityStackProxy(it) }

                        loggerD(msg = "${stackProxy?.instance}")
                        if (stackProxy != null
                            && stackProxy.mTask != instance                 // 排除当前 task
                            && !stackProxy.mIsLaunchingSmallFreeForm        // 检查挂起状态
                            && stackProxy.mMiuiFreeFromWindowMode == 0      // 检查小窗模式
                            && !stackProxy.inPinMode()                      // 检查贴边状态
                            && stackProxy.mTaskAnimationAdapter == null     // 检查当前是否有动画
                            && mWmService != null
                        ) {
                            loggerD(msg = "start ${stackProxy.instance}")
                            // 开始处理 mMiuiFreeFormActivityStackProxy
                            when (prefs.direct().get(DataConst.FREEFORM_LOSE_FOCUS_OPT_TYPE)) {
                                0 -> {}
                                1 -> launchSmallWindow(stackProxy, mWmService)
                                2 -> freeFormToPin(stackProxy, mWmService)
                                3 -> exitApplication(stackProxy, mWmService)
                            }
                        }

                    }
                }
            }
        }

        /**
         * Bug 修复：
         * java.lang.NullPointerException: Attempt to invoke virtual method 'com.android.server.wm.WindowState com.android.server.wm.ActivityRecord.findMainWindow()' on a null object reference
         * at com.android.server.wm.MiuiFreeFormWindowMotionHelper.setAlpha(MiuiFreeFormWindowMotionHelper.java:3661)
         */
        "com.android.server.wm.MiuiFreeFormWindowMotionHelper".hook {
            injectMember {
                method { name("setAlpha") }
                beforeHook {
                    args[0]?.getFieldValueOrNull("mLastIconLayerWindowToken")
                        ?: resultNull()
                }
            }
        }

    }

    /**
     * 小窗失去焦点后，挂起小窗
     */
    private fun launchSmallWindow(stackProxy: MiuiFreeFormActivityStackProxy, mWmService: Any) {
        // 挂起限制
        if (MiuiMultiWindowUtilsProxy.hasSmallFreeform()) return

        /**
         * 设置挂起位置，1 - 左边，2 - 右边
         */
        stackProxy.mCornerPosition = 2
        mWmService.callMethodByName("launchSmallFreeFormWindow", stackProxy.instance, false)
    }

    /**
     * 将小窗放置后台，有Bug，待修复，不建议使用
     */
    private fun moveToBlack(stackProxy: MiuiFreeFormActivityStackProxy, mWmService: Any) {

        // 有动画，但最近任务无法再次进入
        mWmService.getFieldValueOrNull("mMiuiFreeFormGestureController")
            ?.callMethodByName("moveTaskToBack", stackProxy.instance)
        // 设置为全屏小窗标记，修复Home键触发时转成贴边小窗的Bug
        stackProxy.mMiuiFreeFromWindowMode = -1
    }

    /**
     * 关闭小窗官方逻辑，无Bug，但缺少手势动画
     */
    private fun exitApplication(stackProxy: MiuiFreeFormActivityStackProxy, mWmService: Any) {

        // 无动画，无Bug
        mWmService.getFieldValueOrNull("mMiuiFreeFormGestureController")
            ?.getFieldValueOrNull("mGestureListener")
            ?.getFieldValueOrNull("mFreeFormWindowMotionHelper")
            ?.callMethodByName("startExitApplication", stackProxy.instance)
    }

    /**
     * 失去焦点时小窗会贴边
     * @param stackProxy
     */
    private fun freeFormToPin(stackProxy: MiuiFreeFormActivityStackProxy, mWmService: Any) {
        mWmService.getFieldValueOrNull("mMiuiFreeFormGestureController")?.let {
            it.getFieldValueOrNull("mHandler")
                ?.callMethodByName(
                    "postAtFrontOfQueue",
                    Runnable {
                        it.getFieldValueOrNull("mMiuiFreeformPinManagerService")
                            ?.callMethodByName("handleFreeFormToPin", stackProxy.instance)
                    }
                )
        }
    }
}