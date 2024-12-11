package com.xaye.diyview.view.trapezoid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

//梯形
class TrapezoidView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTrapezoid(canvas)
    }

    private fun drawTrapezoid(canvas: Canvas) {
        val width = width
        val height = height

        // 定义梯形的顶点
        val topWidth = width * 0.6f // 顶部边宽度
        val bottomWidth = width.toFloat() // 底部边宽度
        val topStartX = (width - topWidth) / 2
        val topEndX = topStartX + topWidth

        val path = Path().apply {
            moveTo(topStartX, 0f) // 顶部左点
            lineTo(topEndX, 0f)   // 顶部右点
            lineTo(width.toFloat(), height.toFloat()) // 底部右点
            lineTo(0f, height.toFloat()) // 底部左点
            close()
        }

        canvas.drawPath(path, paint)
    }

}
