package com.my.blog.myapplication.span

import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance


class RainbowSpan(context: Context) : CharacterStyle(),
    UpdateAppearance {
    private val colors: IntArray

    init {
        colors = intArrayOf(Color.RED,Color.GREEN)
    }

    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val shader: Shader = LinearGradient(
            0f, 0f, 0f, paint.textSize * colors.size, colors, null,
            Shader.TileMode.MIRROR
        )
        val matrix = Matrix()
        matrix.setRotate(90f)
        shader.setLocalMatrix(matrix)
        paint.shader = shader
    }
}