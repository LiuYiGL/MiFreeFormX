package org.liuyi.mifreeformx.activity.page

import android.annotation.SuppressLint
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.ImageV
import cn.fkj233.ui.activity.view.TextSummaryV
import org.liuyi.mifreeformx.R

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/13:08:21
 * @Description:
 */
@SuppressLint("NonConstantResourceId")
@BMPage(key = "DonatePage", titleId = R.string.donation)
class DonatePage : MyBasePage() {
    override fun onCreate() {
        TitleText(text = "微信赞赏码")
        val wechatCode = activity.resources.getDrawable(R.drawable.wechat_appreciation_code, activity.theme)
        ImageView(wechatCode, size = 300f, position = ImageV.POSITION_CENTER)
        TextSummaryWithArrow(
            TextSummaryV(text = "打开微信") {
                openApp("com.tencent.mm")
            }
        )

        Line()
        TitleText(text = "支付宝")
        val alipayCode = activity.resources.getDrawable(R.drawable.alipay_code, activity.theme)
        ImageView(alipayCode, size = 300f, position = ImageV.POSITION_CENTER)
        TextSummaryWithArrow(
            TextSummaryV(text = "打开支付宝") {
                openApp("com.eg.android.AlipayGphone")
            }
        )
    }
}