package org.liuyi.mifreeformx.xposed.operation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.UserHandle
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.type.android.UserHandleClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import org.liuyi.mifreeformx.proxy.framework.MiuiFreeFormActivityStack
import org.liuyi.mifreeformx.proxy.framework.MiuiMultiWindowUtils
import org.liuyi.mifreeformx.proxy.framework.WindowManagerService
import org.liuyi.mifreeformx.utils.callMethodByName
import org.liuyi.mifreeformx.utils.getFieldValueOrNull

/**
 * @Author: Liuyi
 * @Date: 2023/05/16/17:24:27
 * @Description:
 */
object FreeFormOutsideActionOpt {

    /**
     * 小窗失去焦点后，迷你小窗
     * 使用广播逻辑 具体 com.android.server.wm.MiuiFreeFormGestureController.FreeFormReceiver#onReceive
     */
    @SuppressLint("MissingPermission")
    fun turnFreeFormToSmallWindow(
        stackId: Int,
        mWmService: WindowManagerService
    ) {
        mWmService.mH.post {
            val userHandle = UserHandleClass.constructor { param(IntType) }.get().newInstance<UserHandle>(-1)
            val intent = Intent("com.miui.fullscreen_state_change")
                .putExtra("state", "toSmallFreeform")
                .putExtra("rootStackID", stackId)
            mWmService.mContext?.sendBroadcastAsUser(intent, userHandle)
        }
    }

    /**
     * 小窗失去焦点后，挂起小窗
     */
    fun launchSmallWindow(
        stack: MiuiFreeFormActivityStack,
        mWmService: WindowManagerService
    ) {
        // 挂起限制
        if (MiuiMultiWindowUtils.proxy.hasSmallFreeform()) return

        /**
         * 设置挂起位置，1 - 左边，2 - 右边
         */
        stack.mCornerPosition = 2
        mWmService.launchSmallFreeFormWindow(stack, false)
    }

    /**
     * 关闭小窗官方逻辑，无Bug，但缺少手势动画
     */
    fun exitApplication(
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
    fun exitAllFreeform(mWmService: WindowManagerService) {
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
     fun freeFormToPin(stack: MiuiFreeFormActivityStack, mWmService: WindowManagerService) {
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
    fun allFreeFromToPin(mWmService: WindowManagerService) {
        mWmService.mH.post {
            val userHandle = UserHandleClass.constructor { param(IntType) }.get().newInstance<UserHandle>(-1)
            val intent = Intent("com.miui.fullscreen_state_change")
                .putExtra("state", "toHome")
            mWmService.mContext?.sendBroadcastAsUser(intent, userHandle)
        }
    }
}