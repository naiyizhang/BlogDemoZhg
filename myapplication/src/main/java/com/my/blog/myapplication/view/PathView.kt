package com.my.blog.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class PathView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var mPhase = 0f

    init {
        path.moveTo(100f, 100f)
        path.quadTo(500f,100f,500f, 500f)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.color = Color.RED
        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.RED
        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), mPhase)
        canvas.drawPath(path, paint)
        mPhase += 2f

        canvas.translate(0f,200f)
        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
        paint.color = Color.GREEN
        canvas.drawPath(path, paint)

        postInvalidateOnAnimation()
    }
}