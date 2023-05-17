package org.liuyi.mifreeformx.activity.page

import android.graphics.Rect
import android.view.View
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import org.liuyi.mifreeformx.DataConst
import org.liuyi.mifreeformx.R
import org.liuyi.mifreeformx.proxy.framework.MiuiMultiWindowUtils
import org.liuyi.mifreeformx.utils.RectUtils
import org.liuyi.mifreeformx.xposed.hooker.SizeAndPositionHooker

/**
 * @Author: Liuyi
 * @Date: 2023/05/13/19:25:04
 * @Description:
 */
@BMPage("SizeAndPosition", titleId = R.string.freeform_size_and_position)
class SizeAndPositionPage : MyBasePage() {

    companion object {
        val shortSide = ScreenUtils.getScreenHeight().coerceAtMost(ScreenUtils.getScreenWidth())
        val longSide = ScreenUtils.getScreenWidth().coerceAtLeast(ScreenUtils.getScreenHeight())
    }

    private val defaultVerticalRect by lazy { MiuiMultiWindowUtils.StaticProxy.getFreeformRect(activity, false, true) }
    private val defaultVerticalScale by lazy {
        MiuiMultiWindowUtils.StaticProxy.getFreeFormScale(
            true,
            false,
            MiuiMultiWindowUtils.StaticProxy.getScreenType(activity)
        )
    }

    private val defaultHorizontalRect by lazy { MiuiMultiWindowUtils.StaticProxy.getFreeformRect(activity, false, false) }
    private val defaultHorizontalScale by lazy {
        MiuiMultiWindowUtils.StaticProxy.getFreeFormScale(
            false,
            false,
            MiuiMultiWindowUtils.StaticProxy.getScreenType(activity)
        )
    }


    private var buffer: String? = null


    private val modeViewBinding = GetDataBinding({
        val index = activity.prefs().get(SizeAndPositionHooker.OPEN_CUSTOM_SIZE_AND_POSITION_MODE)
        SizeAndPositionHooker.openCustomSizeAndPositionModeString[index]
    }) { view, flags, data ->
        when (data) {
            SizeAndPositionHooker.openCustomSizeAndPositionModeString[0] -> view.visibility = View.GONE

            SizeAndPositionHooker.openCustomSizeAndPositionModeString[3],
            SizeAndPositionHooker.openCustomSizeAndPositionModeString[flags] -> view.visibility = View.VISIBLE

            else -> view.visibility = View.GONE
        }
    }

    override fun onCreate() {

        TitleText("基础")
        TextSummaryWithSpinner(
            TextSummaryV(
                text = "自定义小窗位置与大小",
            ),
            createSpinnerV(
                SizeAndPositionHooker.OPEN_CUSTOM_SIZE_AND_POSITION_MODE,
                SizeAndPositionHooker.openCustomSizeAndPositionModeString,
                dataBindingSend = modeViewBinding.bindingSend
            ),
        )

        TitleText(text = "竖屏", dataBindingRecv = modeViewBinding.getRecv(1))
        val scaleVerticalRect = defaultVerticalRect?.let { RectUtils.getScaledRect(it, defaultVerticalScale) } ?: Rect()
        TextSA("水平方向", onClickListener = {
            showIntDialog(
                "水平方向",
                0..shortSide,
                -1,
                SizeAndPositionHooker.CUSTOM_POSITION_CENTER_X,
                scaleVerticalRect.centerX()
            )
        }, dataBindingRecv = modeViewBinding.getRecv(1))
        TextSA("竖直方向", onClickListener = {
            showIntDialog(
                "竖直方向",
                0..longSide,
                -1,
                SizeAndPositionHooker.CUSTOM_POSITION_CENTER_Y,
                scaleVerticalRect.centerY()
            )
        }, dataBindingRecv = modeViewBinding.getRecv(1))
        TextSA("自定义缩放比例", onClickListener = {
            showFloatDialog("缩放比例", 0..1, 0, SizeAndPositionHooker.CUSTOM_SCALE, defaultVerticalScale)
        }, dataBindingRecv = modeViewBinding.getRecv(1))

        TitleText(text = "横屏", dataBindingRecv = modeViewBinding.getRecv(2))
        val scaleHorizontalRect =
            defaultHorizontalRect?.let { RectUtils.getScaledRect(it, defaultHorizontalScale) } ?: Rect()
        TextSA("水平方向",
            tips = "横屏的水平方向为较长端",
            onClickListener = {
                showIntDialog(
                    "水平方向",
                    0..longSide,
                    -1,
                    SizeAndPositionHooker.CUSTOM_POSITION_CENTER_HORIZONTAL_X,
                    scaleHorizontalRect.centerX()
                )
            }, dataBindingRecv = modeViewBinding.getRecv(2)
        )
        TextSA("竖直方向", onClickListener = {
            showIntDialog(
                "竖直方向",
                0..shortSide,
                -1,
                SizeAndPositionHooker.CUSTOM_POSITION_CENTER_HORIZONTAL_Y,
                scaleHorizontalRect.centerY()
            )
        }, dataBindingRecv = modeViewBinding.getRecv(2))
        TextSA("自定义缩放比例", onClickListener = {
            showFloatDialog("缩放比例", 0..1, 0, SizeAndPositionHooker.CUSTOM_HORIZONTAL_SCALE, defaultHorizontalScale)
        }, dataBindingRecv = modeViewBinding.getRecv(2))


        Line()
        TitleText("增强")
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.disable_window_offset,
                tipsId = R.string.disable_window_offset_tips
            ),
            createSwitchV(DataConst.DISABLE_MULTI_OFFSET)
        )
    }

    private fun showIntDialog(
        title: CharSequence?,
        range: IntRange,
        defaultKey: Int,
        prefsData: PrefsData<Int>,
        defaultValue: Int
    ) {
        MIUIDialog(activity) {
            setTitle(title)
            setMessage("请输入 ${range.first} 至 ${range.last} 的整数\n输入 $defaultKey 时使用默认位置")
            setEditText("", "当前：${activity.prefs().get(prefsData)}，默认：${defaultValue}") {
                buffer = it
            }
            setLButton(R.string.cancel) { cancel() }
            setRButton(R.string.done) {
                kotlin.runCatching {
                    when (val value = buffer?.toInt()!!) {
                        in range, defaultKey -> {
                            activity.prefs().edit { put(prefsData, value) }
                            cancel()
                        }

                        else -> throw Exception()
                    }
                }.exceptionOrNull()?.let {
                    ToastUtils.showShort("请输入正确的数值")
                }
            }
        }.show()
    }

    private fun showFloatDialog(
        title: CharSequence?, range: IntRange, defaultKey: Int,
        prefsData: PrefsData<Float>, defaultValue: Float
    ) {
        MIUIDialog(activity) {
            setTitle(title)
            setMessage("请输入 ${range.first} 至 ${range.last} 的小数\n当输入 $defaultKey 时使用默认位置")
            setEditText("", "当前：${activity.prefs().get(prefsData)}，默认：$defaultValue") {
                buffer = it
            }
            setLButton(R.string.cancel) { cancel() }
            setRButton(R.string.done) {
                kotlin.runCatching {
                    val float = buffer?.toFloat()!!
                    if (float >= range.first && float < range.last) {
                        activity.prefs().edit { put(prefsData, float) }
                        cancel()
                    } else throw Exception()
                }.exceptionOrNull()?.let {
                    ToastUtils.showShort("请输入正确的数值")
                }
            }
        }.show()
    }

}