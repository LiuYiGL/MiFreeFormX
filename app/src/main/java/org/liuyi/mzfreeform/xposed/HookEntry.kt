package org.liuyi.mzfreeform.xposed

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

/**
 * @Author: Liuyi
 * @Date: 2023/04/18/17:12:07
 * @Description:
 */
@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {
    override fun onHook() {

    }
}