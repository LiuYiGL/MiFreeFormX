package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMMenuPage
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import com.blankj.utilcode.util.ShellUtils
import org.liuyi.mifreeformx.R

/**
 * @Author: Liuyi
 * @Date: 2023/04/27/2:51:02
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMMenuPage(titleId = R.string.menu)
class MenuPage : MyBasePage() {
    override fun onCreate() {
        TextWithArrow(TextV(textId = R.string.reboot) {
            MIUIDialog(activity) {
                setTitle(R.string.reboot)
                setMessage(R.string.reboot_msg)
                setLButton(R.string.cancel) { cancel() }
                setRButton(R.string.done) {
                    ShellUtils.execCmd("reboot", false)
                    cancel()
                }
            }.show()
        })
        TextWithArrow(TextV(textId = R.string.rebootSystemUI) {
            ShellUtils.execCmd("killall android", true)
        })
    }

}