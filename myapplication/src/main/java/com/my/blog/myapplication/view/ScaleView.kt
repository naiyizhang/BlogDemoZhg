package com.my.blog.myapplication.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.my.blog.myapplication.R

class ScaleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val matrix = Matrix()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG)

    private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_5)

    private  val TAG = "ScaleView"
    init {
        val count = attrs?.attributeCount ?: 0
        for (index in 0 until count) {
            val name = attrs?.getAttributeName(index)
            val value = attrs?.getAttributeValue(index)
            Log.e(TAG, "name = $name, value is $value ", )
        }
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 50f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawRect(
//            (width / 2 - 100).toFloat(), (height / 2 - 100).toFloat(),
//            (width / 2 + 100).toFloat(), (height / 2 + 100).toFloat(), paint
//        )
        canvas.drawLine(200f, 0f, 200f, height.toFloat(), paint)
        canvas.drawLine(0f, 100f, width.toFloat(), 100f, paint)
        matrix.reset()
        matrix.postScale(2f, 2f)
        matrix.postTranslate((width / 2f - bitmap.width), (height / 2f - bitmap.height))

        canvas.drawBitmap(bitmap, matrix, null)


    }

}