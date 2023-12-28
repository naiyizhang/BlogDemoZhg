package com.my.blog.myapplication.span

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.util.Log

class MutableForegroundColorSpan(private var color: Int) : ForegroundColorSpan(color) {

    fun setColor(color: Int) {
        this.color = color
    }

    override fun getForegroundColor(): Int {
        Log.e(Companion.TAG, "getForegroundColor: " )
        return color
    }

    override fun updateDrawState(textPaint: TextPaint) {
        Log.e(Companion.TAG, "updateDrawState: " )
        textPaint.color = color
    }

    companion object {
        private const val TAG = "MutableForegroundColorS"
    }
}