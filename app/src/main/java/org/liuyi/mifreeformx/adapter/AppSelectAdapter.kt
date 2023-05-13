package org.liuyi.mifreeformx.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CompoundButton
import android.widget.LinearLayout
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.ImageTextSummaryWithSwitchV
import cn.fkj233.ui.activity.view.ImageV
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.blankj.utilcode.util.AppUtils.AppInfo

/**
 * @Author: Liuyi
 * @Date: 2023/05/08/22:21:49
 * @Description:
 */
class AppSelectAdapter(
    var mContext: Context,
    var mAppInfoList: MutableList<AppInfo>,
    var selectedSet: MutableSet<String>,
    var disEnabledSet: Set<String>,
    private val block: AppSelectAdapter.(Boolean, AppInfo) -> Unit
) : BaseAdapter() {


    override fun getCount() = mAppInfoList.size

    override fun getItem(position: Int) = mAppInfoList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return LinearLayout(mContext).also { layout ->
            mAppInfoList[position].run {
                SwitchV("").let { switchV ->
                    ImageTextSummaryWithSwitchV(
                        ImageV(icon),
                        TextSummaryV(text = name, tips = packageName),
                        switchV
                    ).let {
                        it.onDraw(MIUIFragment(""), layout, it.create(mContext) {})
                        switchV.switch.apply {
                            isChecked = selectedSet.contains(packageName)
                            isEnabled = !disEnabledSet.contains(packageName)
                            setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) selectedSet += this@run.packageName
                                else selectedSet -= this@run.packageName
                                block(this@AppSelectAdapter, isChecked, this@run)
                            }
                        }
                    }
                }
            }

        }
    }
}