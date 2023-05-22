package org.liuyi.mifreeformx.xposed.hooker

import android.annotation.SuppressLint
import android.content.Intent
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.BlackList
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.intent.LyIntent
import org.liuyi.mifreeformx.proxy.framework.ActivityTaskManagerService
import org.liuyi.mifreeformx.proxy.framework.RootWindowContainer
import org.liuyi.mifreeformx.proxy.framework.SafeActivityOptions
import org.liuyi.mifreeformx.utils.*
import org.liuyi.mifreeformx.xposed.base.LyBaseHooker
import org.liuyi.mifreeformx.xposed.operation.AppJumpOpt
import org.liuyi.mifreeformx.xposed.operation.AppShareOpt
import org.liuyi.mifreeformx.xposed.operation.ParallelSmallWindowOpt

/**
 * @Author: Liuyi
 * @Date: 2023/04/22/0:16:36
 * @Description:
 */
@SuppressLint("QueryPermissionsNeeded")

object FrameworkBaseHooker : LyBaseHooker() {

    private const val APK_MIME = "application/vnd.android.package-archive"
    val INSTALL_APP_BY_FREEFORM = PrefsData("install_app_by_freeform", false)


    @SuppressLint("WrongConstant")
    override fun onHook() {

        var rootWindowContainer: RootWindowContainer? = null
        val activityTaskManagerService: ActivityTaskManagerService? = null
        "com.android.server.wm.ActivityTaskManagerService".hook {
            /**
             * 用户应用调用会来到这里
             */
            injectMember {
                method {
                    name("startActivityAsUser")
                    paramCount(13)
                }
                beforeHook {
                    loggerD(msg = "${this.args.asList()}")
                    // 全局管控，只要在intent设置了 FreeFormIntent 都会优先判断是否开启小窗
                    val caller = args[1] as String?
                    val intent = Intent(args[3] as? Intent?)
                    val atmService = activityTaskManagerService ?: instance.getProxyAs()
                    rootWindowContainer = rootWindowContainer ?: atmService.mRootWindowContainer
                    args[3] = intent
                    val context = atmService.mContext ?: return@beforeHook
                    val callee = intent.resolveActivity(context.packageManager)?.packageName ?: return@beforeHook
                    if (intent.type == APK_MIME) {
                        if (prefs.get(INSTALL_APP_BY_FREEFORM)) {
                            logD("判断为安装应用")
                            intent.addFlags(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        return@beforeHook
                    }

                    if (prefs.get(DataConst.PARALLEL_MULTI_WINDOW_PLUS)
                        && ParallelSmallWindowOpt.isFromSidebar(caller, intent)
                        && BlackList.ParallelFreeformWhitelist.contains(prefs, callee)
                    ) {
                        logD("侧边栏打开平行小窗")
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        return@beforeHook
                    }

                    if (prefs.get(DataConst.APP_JUMP)
                        && AppJumpOpt.isAppJump(args[0], caller, intent, context)
                        && caller != null
                        && !BlackList.AppJumpSourceBlacklist.contains(prefs, caller)
                        && !BlackList.AppJumpTargetBlacklist.contains(prefs, callee)
                    ) {
                        logD("判断为应用间跳转")
                        intent.addFlags(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM)
                    }

                    if (prefs.get(DataConst.SHARE_TO_APP)
                        && AppShareOpt.isShareToApp(args[1] as? String?, intent)
                        && caller != null
                        && !BlackList.AppShareSourceBlacklist.contains(prefs, caller)
                        && !BlackList.AppShareTargetBlacklist.contains(prefs, callee)
                    ) {
                        logD("判断为应用间分享")
                        AppShareOpt.handle(intent)
                    }
                }
            }
            /**
             * 系统分享时会来到这里
             */
            injectMember {
                method { name("startActivityAsCaller") }
                beforeHook {
                    val atmService = activityTaskManagerService ?: instance.getProxyAs()
                    val context = atmService.mContext ?: return@beforeHook
                    val intent = Intent(args[2] as? Intent?)
                    val caller = args[1] as? String?
                    args[2] = intent

                    if (prefs.get(DataConst.SHARE_TO_APP)
                        && AppShareOpt.isShareToApp(caller, intent)
                        && !BlackList.AppShareTargetBlacklist.contains(prefs, intent, context)
                    ) {
                        logD("判断为系统的应用间分享")
                        AppShareOpt.handle(intent)
                    }
                }
            }
        }

        /**
         * 对 系统 启动的Activity进行 管控，系统PendingIntent会send到这里，通常都是通知
         * 1. 通知
         * 2. 非双开的微信分享
         * 3. 媒体存储设备的活动
         * "com.android.server.wm.ActivityStartController#startActivityInPackage"
         */
        "com.android.server.wm.ActivityStartController".hook {
            injectMember {
                method { name("startActivityInPackage") }
                beforeHook {
                    loggerD(msg = "${args.asList()}")
                    val intent = Intent(args[5] as? Intent?)
                    args[5] = intent
                    val realCallingPid = args[1] as Int
                    val atmService = activityTaskManagerService ?: instance.getFieldValueByName("mService").getProxyAs()
                    val caller = args[3] as? String?
                    val context = atmService.mContext ?: return@beforeHook
                    val safeActivityOptions = args[11]?.getProxyAs<SafeActivityOptions>() ?: return@beforeHook
                    if (AppShareOpt.isShareToApp(caller, intent) && prefs.get(DataConst.SHARE_TO_APP)
                        && !BlackList.AppShareTargetBlacklist.contains(prefs, intent, context)
                    ) {
                        logD("判断为应用间分享")
                        intent.addFlags(LyIntent.FLAG_ACTIVITY_OPEN_FREEFORM)
                    }
                    if (
                        prefs.get(DataConst.PARALLEL_MULTI_WINDOW_PLUS)
                        && realCallingPid == context.getPidFromPackageNameExt("com.android.systemui")
                        && BlackList.ParallelFreeformWhitelist.contains(prefs, intent, context)
                        && safeActivityOptions.mOriginalOptions?.getLaunchWindowingModeExt() == 5
                    ) {
                        logD("通知打开平行小窗")
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    }
                }
            }
        }

    }


}
