package com.my.blog.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.my.blog.myapplication.databinding.ActivityLottieBinding

class LottieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLottieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            binding.lottie.setAnimation(R.raw.prob1)
            binding.lottie.playAnimation()
        }
    }
}