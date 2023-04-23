package org.liuyi.mzfreeform.activity

import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import org.liuyi.mzfreeform.page.LightOpen
import org.liuyi.mzfreeform.page.MainPage
import org.liuyi.mzfreeform.page.NotificationAndControlCenterPage
import org.liuyi.mzfreeform.page.ShareToAppPage

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:27:44
 * @Description:
 */
class MainActivity : MIUIActivity() {

    init {
        registerPage(MainPage::class.java)
        registerPage(LightOpen::class.java)
        registerPage(NotificationAndControlCenterPage::class.java)
        registerPage(ShareToAppPage::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}