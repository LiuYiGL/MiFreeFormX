package org.liuyi.mifreeformx.activity

import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import com.blankj.utilcode.util.ToastUtils
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.factory.prefs
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.activity.page.AboutPage
import org.liuyi.mifreeformx.activity.page.AppSelectPage
import org.liuyi.mifreeformx.activity.page.DonatePage
import org.liuyi.mifreeformx.activity.page.IgnorePopViewPage
import org.liuyi.mifreeformx.activity.page.JumpAndShareBetweenApplicationsPage
import org.liuyi.mifreeformx.activity.page.MainPage
import org.liuyi.mifreeformx.activity.page.MenuPage
import org.liuyi.mifreeformx.activity.page.MiscellaneousPage
import org.liuyi.mifreeformx.activity.page.NotificationAndControlCenterPage
import org.liuyi.mifreeformx.activity.page.ParallelSmallWindowPage
import org.liuyi.mifreeformx.activity.page.SizeAndPositionPage

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:27:44
 * @Description:
 */
class MainActivity : MIUIActivity() {

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
        registerPage(SizeAndPositionPage::class.java)
        registerPage(IgnorePopViewPage::class.java)
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
        if (!prefs().isPreferencesAvailable) {
            ToastUtils.showShort("模块配置获取失败，请检查环境并尝试重启模块或系统")
        }
        setSP(getPreferences(MODE_PRIVATE))
        super.onCreate(savedInstanceState)
    }
}