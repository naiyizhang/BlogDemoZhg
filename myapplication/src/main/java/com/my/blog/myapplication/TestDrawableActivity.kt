package com.my.blog.myapplication

import android.animation.AnimatorInflater
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedStateListDrawable
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class TestDrawableActivity : AppCompatActivity() {

    private  val TAG = "TestDrawableActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_drawable)

        val button = findViewById<Button>(R.id.button)
        button.isSelected = true

        button.setOnClickListener {
            button.isSelected = !button.isSelected
            val bg = button.background
            bg.level = bg.level+1000

        }

//        bg.start()
//        Log.e(TAG, "onCreate: $bg" )
//        bg.setLevel(9000)

        Log.e(TAG, "onCreate: ${findViewById<ProgressBar>(R.id.progressbar).indeterminateDrawable}", )

    }
}