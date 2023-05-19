package org.liuyi.mifreeformx.utils

import android.app.Activity
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.log.loggerI
import com.highcapable.yukihookapi.hook.log.loggerW
import com.highcapable.yukihookapi.hook.param.HookParam
import com.highcapable.yukihookapi.hook.param.PackageParam

/**
 * @Author: Liuyi
 * @Date: 2023/05/17/14:58:57
 * @Description:
 */

fun Activity.logI(msg: String) = loggerI(msg = "[${javaClass.simpleName}] $msg")
fun Activity.logD(msg: String) = loggerD(msg = "[${javaClass.simpleName}] $msg")
fun Activity.logW(msg: String) = loggerW(msg = "[${javaClass.simpleName}] $msg")
fun Activity.logE(msg: String = "", e: Throwable? = null) = loggerE(msg = "[${javaClass.simpleName}] $msg", e = e)


fun PackageParam.logI(msg: String) = loggerI(msg = "[${javaClass.simpleName}] $msg")
fun PackageParam.logD(msg: String) = loggerD(msg = "[${javaClass.simpleName}] $msg")
fun PackageParam.logW(msg: String) = loggerW(msg = "[${javaClass.simpleName}] $msg")
fun PackageParam.logE(msg: String = "", e: Throwable? = null) = loggerE(msg = "[${javaClass.simpleName}] $msg", e = e)

fun HookParam.logI(msg: String) = loggerI(msg = "[${javaClass.simpleName}] [${member.name}] $msg")
fun HookParam.logD(msg: String) = loggerD(msg = "[${javaClass.simpleName}] [${member.name}] $msg")
fun HookParam.logW(msg: String) = loggerW(msg = "[${javaClass.simpleName}] [${member.name}] $msg")
fun HookParam.logE(msg: String = "", e: Throwable? = null) = loggerE(msg = "[${javaClass.simpleName}] [${member.name}] $msg", e = e)

fun HookParam.logStack() = this.runCatching { throw Exception() }.onFailure { logE("logStack", it) }

