package com.my.blog.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.my.blog.myapplication.R
import com.my.blog.myapplication.databinding.ActivityTestBactivityBinding

class TestBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            startActivity(Intent(this,TestCActivity::class.java))
        }
    }
}