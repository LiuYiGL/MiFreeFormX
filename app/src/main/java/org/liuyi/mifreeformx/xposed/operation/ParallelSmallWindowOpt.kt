package org.liuyi.mifreeformx.xposed.operation

import android.content.Intent
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.param.HookParam
import org.liuyi.mifreeformx.proxy.framework.ActivityTaskManagerService
import org.liuyi.mifreeformx.proxy.framework.RootWindowContainer
import org.liuyi.mifreeformx.utils.getFieldValueByName
import org.liuyi.mifreeformx.utils.isSameApp
import org.liuyi.mifreeformx.utils.logD

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/7:08:07
 * @Description:
 */
object ParallelSmallWindowOpt {

    /**
     *  判断是否来自侧边栏
     *  逻辑来自手机管家
     */
    internal fun isFromSidebar(callingPackage: String?, intent: Intent): Boolean {
        return callingPackage == "com.miui.securitycenter"
                && !intent.isSameApp(callingPackage)
                && intent.action == Intent.ACTION_MAIN
                && intent.categories.contains(Intent.CATEGORY_LAUNCHER)
                && intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0
    }

    /**
     *
     * @param intent
     * @param atmService
     * @param container
     * @return 是否运行其他的分支
     */
    internal fun handle(
        intent: Intent,
        atmService: ActivityTaskManagerService,
        container: RootWindowContainer?
    ): Boolean {
        loggerD(msg = "开启平行小窗: $this")
        atmService.getTasks(Int.MAX_VALUE).orEmpty().lastOrNull { taskInfo ->
            // 获得栈底最后一个符合条件的task
            taskInfo != null && taskInfo.baseActivity == intent.component
                    && taskInfo.isRunning && taskInfo.topActivity == intent.component
        }?.also {
            // 如果获取到了将其放入堆栈前
            loggerD(msg = "已存在一个任务: $it")
            container?.anyTaskForId(it.taskId)?.moveToFront("god let me do it")
        } ?: intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        return false
    }

    internal fun HookParam.parallelSmallWindowOpt(intent: Intent, callee: String, atmService: ActivityTaskManagerService) {
        val taskInfos = atmService.getAllRootTaskInfos().orEmpty().filterNotNull()
            .filter { it.getFieldValueByName("visible") as Boolean }
        val isExistsSameApp = taskInfos.any { taskInfo -> taskInfo.baseActivity?.packageName == callee }
        if (isExistsSameApp) {
            logD("存在相同的可视应用：$callee")
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
    }
}