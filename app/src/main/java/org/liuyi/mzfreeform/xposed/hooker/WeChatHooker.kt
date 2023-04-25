package org.liuyi.mzfreeform.xposed.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import org.liuyi.mzfreeform.DataConst
import org.liuyi.mzfreeform.utils.by

/**
 * @Author: Liuyi
 * @Date: 2023/04/25/15:50:24
 * @Description:
 */
object WeChatHooker : YukiBaseHooker() {
    override fun onHook() {
        /**
         * 实现微信平行小窗Plus
         */
        "com.tencent.mm.ui.LauncherUI".hook {
            injectMember {
                method { name("onCreate") }
                beforeHook {
                    by(this, DataConst.PARALLEL_MULTI_WINDOW_PLUS) {
                        instanceClass.field { type(ArrayListClass) }.giveAll().forEach {
                            val any = it.get(null)
                            loggerD(msg = "$any")
                            (any as? MutableList<*>)?.clear()
                        }
                    }
                }
            }
        }
    }


}