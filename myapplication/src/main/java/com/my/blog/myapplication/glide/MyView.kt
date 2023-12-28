package com.my.blog.myapplication.glide

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.view.View

class MyView @JvmOverloads constructor(context:Context):View(context) {
    init {
        val c = Canvas()
        val p = Paint()

        parent.requestDisallowInterceptTouchEvent(true)

    }
}

fun main() {
    println("a")
}