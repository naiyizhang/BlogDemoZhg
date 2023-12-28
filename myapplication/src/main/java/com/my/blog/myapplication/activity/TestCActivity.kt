package com.my.blog.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import com.my.blog.myapplication.R
import com.my.blog.myapplication.databinding.ActivityTestCactivityBinding

class TestCActivity : AppCompatActivity() {
    var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
//            startActivity(Intent(this, TestAActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            })
            if (flag) {
                binding.image.startAnimation(createScaleAnimation())
            } else {
                binding.image.startAnimation(createTranslateAnimation())
            }
            flag = !flag

        }
    }

    private fun createScaleAnimation(): Animation {
        val scaleAnimation = ScaleAnimation(
            1f,
            1.5f,
            1f,
            1.5f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        scaleAnimation.fillAfter = true
        scaleAnimation.duration = 500
        return scaleAnimation
    }

    private fun createTranslateAnimation(): Animation {
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            1f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            1f
        )
        translateAnimation.fillAfter = true
        translateAnimation.duration = 500
        return translateAnimation
    }
}