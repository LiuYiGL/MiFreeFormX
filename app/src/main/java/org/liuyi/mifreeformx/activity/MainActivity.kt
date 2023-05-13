package org.liuyi.mifreeformx.activity

import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import com.blankj.utilcode.util.AppUtils
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.factory.prefs
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.activity.page.*

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:27:44
 * @Description:
 */
class MainActivity : MIUIActivity() {

    companion object {
        lateinit var appsInfo: Any
    }

    init {
        registerPage(MainPage::class.java)
        registerPage(NotificationAndControlCenterPage::class.java)
        registerPage(JumpAndShareBetweenApplicationsPage::class.java)
        registerPage(ParallelSmallWindowPage::class.java)
        registerPage(MiscellaneousPage::class.java)
        registerPage(MenuPage::class.java)
        registerPage(AboutPage::class.java)
        registerPage(AppSelectPage::class.java)
        registerPage(DonatePage::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!YukiHookAPI.Status.isXposedModuleActive) {
            MIUIDialog(this) {
                setTitle(R.string.warning)
                setMessage(R.string.not_support)
                setRButton(R.string.done) {
                    cancel()
                }
            }.show()
        }
        setSP(getPreferences(MODE_PRIVATE))
        super.onCreate(savedInstanceState)
    }
}