package org.liuyi.mifreeformx.adapter

import android.content.ComponentName
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextSummaryWithSwitchV
import cn.fkj233.ui.switch.MIUISwitch
import org.liuyi.mifreeformx.bean.TileInfo

/**
 * @Author: Liuyi
 * @Date: 2023/06/04/2:13:38
 * @Description:
 */
class TileSelectAdapter(
    val mContext: Context,
    var mTileInfos: Array<TileInfo>,
    val initData: ((TileInfo, MIUISwitch) -> Unit)? = null
) : BaseAdapter() {
    override fun getCount() = mTileInfos.size

    override fun getItem(position: Int) = mTileInfos[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return LinearLayout(mContext).also { layout ->
            mTileInfos[position].run {
                val appName = mFlattenShortString?.let {
                    val componentName = ComponentName.unflattenFromString(it)!!
                    mContext.packageManager.getPackageInfo(componentName.packageName, 0)
                }?.applicationInfo?.loadLabel(mContext.packageManager)?.toString() ?: ""
                SwitchV("").let { switchV ->
                    TextSummaryWithSwitchV(
                        TextSummaryV(text = this.mLabel, tips = appName),
                        switchV
                    ).let {
                        it.onDraw(MIUIFragment(""), layout, it.create(mContext) {})
                        initData?.invoke(this, switchV.switch)
                    }
                }
            }

        }
    }
}