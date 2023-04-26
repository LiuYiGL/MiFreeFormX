package org.liuyi.mzfreeform.activity.page

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mzfreeform.R

/**
 * @Author: Liuyi
 * @Date: 2023/04/23/22:30:34
 * @Description:
 */
abstract class MyBasePage : BasePage() {

    internal fun createSwitchV(prefsData: PrefsData<Boolean>) =
        SwitchV(prefsData.key, activity.prefs().get(prefsData)) {
            activity.prefs().edit { put(prefsData, it) }
        }

    internal fun openFunText(@StringRes textId: Int) = getString(R.string.open) + getString(textId)
}