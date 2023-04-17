package org.liuyi.mzfreeform.activity

import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import org.liuyi.mzfreeform.page.MainPage

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/2:27:44
 * @Description:
 */
class MainActivity : MIUIActivity() {

    init {
        registerPage(MainPage::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setSP(getPreferences(MODE_PRIVATE))
        super.onCreate(savedInstanceState)
    }
}