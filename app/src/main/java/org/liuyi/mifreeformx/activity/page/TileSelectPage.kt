package org.liuyi.mifreeformx.activity.page

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.fragment.MIUIFragment
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.factory.prefs
import org.liuyi.mifreeformx.adapter.TileSelectAdapter
import org.liuyi.mifreeformx.utils.logD
import org.liuyi.mifreeformx.xposed.hooker.systemui.LongClickTileHooker

/**
 * @Author: Liuyi
 * @Date: 2023/06/01/10:51:34
 * @Description:
 */
@BMPage(key = "TileSelectPage", title = "选择磁贴")
class TileSelectPage : MyBasePage() {

    init {
        skipLoadItem = true
    }

    var mAdapter: TileSelectAdapter? = null

    override fun onCreate() {
        mAdapter?.let {
            List {
                adapter = mAdapter
            }
        }
    }

    override fun asyncInit(fragment: MIUIFragment) {
        activity.dataChannel("com.android.systemui").put(LongClickTileHooker.DATA_CHANNEL_GET_LIST_KEY)
        activity.dataChannel("com.android.systemui").wait(LongClickTileHooker.DATA_CHANNEL_LIST) {
            logD("${it.toList()}")
            mAdapter = TileSelectAdapter(activity, it) { tileInfo, miuiSwitch ->
                miuiSwitch.apply {
                    LongClickTileHooker.ComponentList.let { list ->
                        isChecked = list.contains(activity.prefs(), tileInfo.mFlattenShortString)
                        setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) list.add(activity.prefs(), tileInfo.mFlattenShortString)
                            else list.remove(activity.prefs(), tileInfo.mFlattenShortString)
                        }
                    }
                }
            }
            fragment.initData()
        }
    }
}