package org.liuyi.mifreeformx.activity.page

import androidx.annotation.StringRes
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.SwitchV
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.R

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

    internal fun createSpinnerV(prefsData: PrefsData<Int>, textList: List<String>) =
        SpinnerV(currentValue = textList[activity.prefs().get(prefsData)]) {
            textList.forEachIndexed { index, text ->
                add(text) { activity.prefs().edit { put(prefsData, index) } }
            }
        }

    internal fun openFunText(@StringRes textId: Int) = getString(R.string.open) + getString(textId)
}