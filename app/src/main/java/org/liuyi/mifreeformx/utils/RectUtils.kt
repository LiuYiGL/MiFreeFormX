package org.liuyi.mifreeformx.utils

import android.graphics.Rect
import kotlin.math.roundToInt

/**
 * @Author: Liuyi
 * @Date: 2023/05/14/22:12:17
 * @Description:
 */
object RectUtils {


    fun scaleRectWithCenter(scaledRect: Rect, oldScale: Float, newScale: Float): Rect {
        val increaseScale = (newScale - oldScale) / 2
        val newLeft = scaledRect.left - increaseScale * scaledRect.width()
        val newRight = scaledRect.right - increaseScale * scaledRect.width()
        val newTop = scaledRect.top - increaseScale * scaledRect.height()
        val newBottom = scaledRect.bottom - increaseScale * scaledRect.height()
        return Rect(newLeft.roundToInt(), newTop.roundToInt(), newRight.roundToInt(), newBottom.roundToInt())
    }

    fun moveRectWithCenter(scaledRect: Rect, scale: Float, centerX: Int, centerY: Int) {
        val height = scaledRect.height()
        val width = scaledRect.width()
        val sourceLeft = scaledRect.left
        val sourceTop = scaledRect.top
        scaledRect.apply {
            if (centerX != -1) {
                left += (centerX - width * scale / 2 - sourceLeft).roundToInt()
                right += (centerX - width * scale / 2 - sourceLeft).roundToInt()
            }
            if (centerY != -1) {
                top += (centerY - height * scale / 2 - sourceTop).roundToInt()
                bottom += (centerY - height * scale / 2 - sourceTop).roundToInt()
            }
        }
    }

    fun getScaledRect(rect: Rect, scale: Float): Rect {
        return Rect(
            rect.left,
            rect.top,
            (rect.left + rect.width() * scale).roundToInt(),
            (rect.top + rect.height() * scale).roundToInt()
        )
    }
}