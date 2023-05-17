package org.liuyi.mifreeformx.xposed.hooker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.UserHandle
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.UserHandleClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.proxy.framework.MiuiFreeFormActivityStack
import org.liuyi.mifreeformx.proxy.framework.MiuiMultiWindowUtils
import org.liuyi.mifreeformx.proxy.framework.Task
import org.liuyi.mifreeformx.proxy.framework.WindowManagerService
import org.liuyi.mifreeformx.utils.callMethodByName
import org.liuyi.mifreeformx.utils.getFieldValueOrNull
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/02/4:23:40
 * @Description: 小窗失去焦点时行为
 */
object FreeformLoseFocusHooker : LyBaseHooker() {
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
                        val task = getProxy<Task>(instance)
                        /**
                         * 排除在桌面上的事件，
                         * 可以修复最近任务无法打开小窗，因为最近任务打开小窗后桌面会申请聚焦
                         * 单例通知时回缩界面导致桌面聚焦
                         */
                        if (task.getActivityType() == 2 ||
                            prefs.get(DataConst.FREEFORM_LOSE_FOCUS_OPT_TYPE) == 0
                        ) return@afterHook

                        val mWmService = task.mWmService
                        val stackProxy = task.mAtmService
                            ?.mMiuiFreeFormManagerService
                            ?.getTopFreeFormActivityStack()

                        loggerD(msg = "${stackProxy?.instance}")
                        if (stackProxy?.instance != null
                            && stackProxy.mTask?.instance != instance       // 排除当前 task
                            && !stackProxy.mIsLaunchingSmallFreeForm        // 检查挂起状态
                            && stackProxy.mMiuiFreeFromWindowMode == 0      // 检查小窗模式
                            && !stackProxy.inPinMode()                      // 检查贴边状态
                            && stackProxy.mTaskAnimationAdapter == null     // 检查当前是否有动画
                            && mWmService?.instance != null
                        ) {
                            loggerD(msg = "start ${stackProxy.instance}")
                            // 开始处理 mMiuiFreeFormActivityStackProxy
                            when (prefs.get(DataConst.FREEFORM_LOSE_FOCUS_OPT_TYPE)) {
                                1 -> turnFreeFormToSmallWindow(stackProxy, mWmService)
                                2 -> allFreeFromToPin(mWmService)
                                3 -> exitAllFreeform(mWmService)
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * 小窗失去焦点后，迷你小窗
     * 使用广播逻辑 具体 com.android.server.wm.MiuiFreeFormGestureController.FreeFormReceiver#onReceive
     */
    @SuppressLint("MissingPermission")
    private fun turnFreeFormToSmallWindow(
        stack: MiuiFreeFormActivityStack,
        mWmService: WindowManagerService
    ) {
        mWmService.mH.post {
            val userHandle = UserHandleClass.constructor { param(IntType) }.get().newInstance<UserHandle>(-1)
            val intent = Intent("com.miui.fullscreen_state_change")
                .putExtra("state", "toSmallFreeform")
                .putExtra("rootStackID", stack.mStackID)
            mWmService.mContext?.sendBroadcastAsUser(intent, userHandle)
        }
    }

    /**
     * 小窗失去焦点后，挂起小窗
     */
    private fun launchSmallWindow(
        stack: MiuiFreeFormActivityStack,
        mWmService: WindowManagerService
    ) {
        // 挂起限制
        if (MiuiMultiWindowUtils.StaticProxy.hasSmallFreeform()) return

        /**
         * 设置挂起位置，1 - 左边，2 - 右边
         */
        stack.mCornerPosition = 2
        mWmService.launchSmallFreeFormWindow(stack, false)
    }

    /**
     * 关闭小窗官方逻辑，无Bug，但缺少手势动画
     */
    private fun exitApplication(
        stack: MiuiFreeFormActivityStack,
        mWmService: WindowManagerService
    ) {

        // 无动画，无Bug
        mWmService.mMiuiFreeFormGestureController
            ?.getFieldValueOrNull("mGestureListener")
            ?.getFieldValueOrNull("mFreeFormWindowMotionHelper")
            ?.callMethodByName("startExitApplication", stack.instance)
    }

    /**
     *
     *
     * @param mWmService
     */
    @SuppressLint("MissingPermission")
    private fun exitAllFreeform(mWmService: WindowManagerService) {
        mWmService.mH.post {
            val userHandle = UserHandleClass.constructor { param(IntType) }.get().newInstance<UserHandle>(-1)
            val intent = Intent("miui.intent.action.PC_MODE_ENTER")
            mWmService.mContext?.sendBroadcastAsUser(intent, userHandle)
        }
    }

    /**
     * 失去焦点时小窗会贴边
     * @param stackProxy
     */
    private fun freeFormToPin(stack: MiuiFreeFormActivityStack, mWmService: WindowManagerService) {
        mWmService.mMiuiFreeFormGestureController?.let {
            it.getFieldValueOrNull("mHandler")
                ?.callMethodByName(
                    "postAtFrontOfQueue",
                    Runnable {
                        it.getFieldValueOrNull("mMiuiFreeformPinManagerService")
                            ?.callMethodByName("handleFreeFormToPin", stack.instance)
                    }
                )
        }
    }

    /**
     * 失去焦点时全部小窗贴边
     * 使用广播逻辑 具体 com.android.server.wm.MiuiFreeFormGestureController.FreeFormReceiver#onReceive
     */
    @SuppressLint("MissingPermission")
    private fun allFreeFromToPin(mWmService: WindowManagerService) {
        mWmService.mH.post {
            val userHandle = UserHandleClass.constructor { param(IntType) }.get().newInstance<UserHandle>(-1)
            val intent = Intent("com.miui.fullscreen_state_change")
                .putExtra("state", "toHome")
            mWmService.mContext?.sendBroadcastAsUser(intent, userHandle)
        }
    }
}