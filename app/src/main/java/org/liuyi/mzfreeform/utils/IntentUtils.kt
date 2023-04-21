package org.liuyi.mzfreeform.utils

import android.app.PendingIntent
import android.content.Intent
import com.highcapable.yukihookapi.hook.factory.method

/**
 * @Author: Liuyi
 * @Date: 2023/04/20/15:46:52
 * @Description:
 */
class IntentUtils {
}

fun PendingIntent.getIntentExt() =
    PendingIntent::class.java.method { name("getIntent") }.get(this).call() as Intent