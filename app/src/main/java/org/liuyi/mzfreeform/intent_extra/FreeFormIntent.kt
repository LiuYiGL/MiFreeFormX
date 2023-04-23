package org.liuyi.mzfreeform.intent_extra

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.liuyi.mzfreeform.intent_extra.FreeFormIntent.FREE_FORM_EXTRA_FORCE
import org.liuyi.mzfreeform.intent_extra.FreeFormIntent.FREE_FORM_EXTRA_IGNORE
import org.liuyi.mzfreeform.intent_extra.FreeFormIntent.INTENT_FREE_FORM_EXTRA_KEY
import org.liuyi.mzfreeform.utils.toMultiWidow

/**
 * @Author: Liuyi
 * @Date: 2023/04/23/0:09:08
 * @Description: 设置Intent Extra 在startActivity 中判断是否使用小窗打开
 */
object FreeFormIntent {
    const val INTENT_FREE_FORM_EXTRA_KEY = "com.mz.freeform.enable"
    const val DEFAULT = 0
    const val FREE_FORM_EXTRA_FORCE = 1
    const val FREE_FORM_EXTRA_IGNORE = 2
}

fun Intent.forceFreeFromMode(isOpen: Boolean = true) =
    if (isOpen) putExtra(INTENT_FREE_FORM_EXTRA_KEY, FREE_FORM_EXTRA_FORCE)
    else putExtra(INTENT_FREE_FORM_EXTRA_KEY, FREE_FORM_EXTRA_IGNORE)

fun Intent.getFreeFormMode() = getIntExtra(INTENT_FREE_FORM_EXTRA_KEY, 0)

fun Intent.setFreeFromBundle(bundle: Bundle, context: Context) {
    if (getFreeFormMode() == FREE_FORM_EXTRA_FORCE) bundle.toMultiWidow(context)
}