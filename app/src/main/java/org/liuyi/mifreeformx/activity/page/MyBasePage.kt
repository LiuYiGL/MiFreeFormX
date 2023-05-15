package org.liuyi.mifreeformx.activity.page

import android.widget.Toast
import androidx.annotation.StringRes
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.SwitchV
import com.blankj.utilcode.util.AppUtils
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

    internal fun createSpinnerV(
        prefsData: PrefsData<Int>,
        textList: List<String>,
        dataBindingSend: DataBinding.Binding.Send? = null,
        dataBindingRecv: DataBinding.Binding.Recv? = null,
    ) =
        SpinnerV(
            currentValue = textList[activity.prefs().get(prefsData)],
            dataBindingSend = dataBindingSend,
            dataBindingRecv = dataBindingRecv
        ) {
            textList.forEachIndexed { index, text ->
                add(text) { activity.prefs().edit { put(prefsData, index) } }
            }
        }

    internal fun openApp(packageName: String) {
        if (AppUtils.isAppInstalled(packageName)) {
            // 已安装，跳转到应用
            val intent = activity.packageManager.getLaunchIntentForPackage(packageName)
            activity.startActivity(intent)
        } else {
            // 未安装，提示用户安装
            Toast.makeText(activity, "请先安装$packageName", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun openFunText(@StringRes textId: Int) = getString(R.string.open) + getString(textId)
}