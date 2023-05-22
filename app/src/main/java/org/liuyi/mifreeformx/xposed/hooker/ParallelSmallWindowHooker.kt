package org.liuyi.mifreeformx.xposed.hooker

import android.app.ActivityManager
import android.content.ComponentName
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.proxy.framework.MiuiFreeFormActivityStack
import org.liuyi.mifreeformx.utils.getFieldValue
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/22/12:43:56
 * @Description:
 */
object ParallelSmallWindowHooker : LyBaseHooker() {
    override fun onHook() {
        "com.android.server.wm.MiuiFreeFormActivityStack".hook {
            injectMember {
                method { name = "onMiuiFreeFormStasckremove" }
                beforeHook {
                    if (!prefs.get(DataConst.PARALLEL_MULTI_WINDOW_PLUS)) return@beforeHook
                    val task = instance.getProxyAs<MiuiFreeFormActivityStack>().mTask ?: return@beforeHook
                    logD("task: $task")
                    if (!BlackList.ParallelFreeformWhitelist.contains(prefs, task.getPackageName()!!)) return@beforeHook

                    val atmService = task.mAtmService!!
                    val runningTaskInfos = atmService.getTasks(Int.MAX_VALUE).orEmpty().filterNotNull()
                    logD("runningTaskInfos: ${buildString { runningTaskInfos.forEach { append("$it\n") } }}")

                    runningTaskInfos.firstOrNull {
                        it.taskId != task.mTaskId
                                && it.getUserIdExt() == task.mUserId
                                && it.getRealActivityExt() == task.realActivity
                    }?.let {
                        logD("找到历史taskInfo: $it")
                        val rootTask = atmService.mRootWindowContainer?.getRootTask(it.taskId) ?: return@beforeHook
                        logD("找到匹配的task: $rootTask")
                        val topNonFinishingActivity = task.getTopNonFinishingActivity() ?: return@beforeHook
                        logD("topNonFinishingActivity: $topNonFinishingActivity")
                        topNonFinishingActivity.reparent(rootTask, Int.MIN_VALUE, "god let me do it")
                    }
                }
            }
        }
    }

    private fun ActivityManager.RunningTaskInfo.getUserIdExt() = getFieldValue {
        name = "userId"
        superClass()
    } as Int

    private fun ActivityManager.RunningTaskInfo.getRealActivityExt() = getFieldValue {
        name = "realActivity"
        superClass()
    } as ComponentName
}