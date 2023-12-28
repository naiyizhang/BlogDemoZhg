package com.my.blog.myapplication.span

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.util.Log

class FrameSpan() : ReplacementSpan() {
    private var gapWidth = 10f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var mWidth = 0f


    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val width = paint.measureText(text, start, end)
        mWidth = width + gapWidth
        if (fm != null) {
            val fontMetrics = paint.fontMetrics
            val height = fontMetrics.bottom - fontMetrics.top
            if (mWidth > height) {
                val gap = (mWidth - height) / 2
                fm.bottom = (fontMetrics.bottom + gap).toInt()
                fm.descent = (fontMetrics.descent + gap).toInt()
                fm.top = (fontMetrics.top - gap).toInt()
                fm.ascent = (fontMetrics.ascent - gap).toInt()
            }
        }
        Log.e(TAG, "after.getSize: $fm ,this = ${this}")

        return mWidth.toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        canvas.drawRoundRect(x, top.toFloat(), x + mWidth, bottom.toFloat(), 10f, 10f, this.paint)
        canvas.drawText(text, start, end, x + gapWidth / 2f, y.toFloat(), paint)
    }


    companion object {
        private const val TAG = "FrameSpan"
    }
}