package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMMenuPage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import com.blankj.utilcode.util.ShellUtils
import com.highcapable.yukihookapi.hook.factory.dataChannel
import org.liuyi.mifreeformx.DataConst
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
                    ShellUtils.execCmd("reboot", true)
                    cancel()
                }
            }.show()
        })
        TitleText("日志")
        TextSummaryWithSwitch(
            TextSummaryV("详细日志", tips = "关闭详细日志可提升部分性能"),
            createSwitchV(DataConst.DETAILED_LOG) { isOpen ->
                activity.resources.getStringArray(R.array.xposedscope).forEach {
                    activity.dataChannel(it).put(DataConst.DETAILED_LOG.key, isOpen)
                }
            }
        )
    }

}