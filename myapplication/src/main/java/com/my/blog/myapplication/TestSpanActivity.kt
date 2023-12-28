package com.my.blog.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import com.my.blog.myapplication.databinding.ActivityTestFloatViewBinding
import com.my.blog.myapplication.databinding.ActivityTestSpanBinding
import com.my.blog.myapplication.span.FrameSpan
import com.my.blog.myapplication.span.MutableForegroundColorSpan
import com.my.blog.myapplication.span.RainbowSpan
import java.util.Collections

class TestSpanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTestSpanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder = SpannableStringBuilder("test123")
        val colorSpan = MutableForegroundColorSpan(Color.RED)
        builder.setSpan(RainbowSpan(this),0,4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView.text = builder
        binding.button.setOnClickListener {
//            colorSpan.setColor(Color.GREEN)
//            binding.textView.text = builder

//            val builder = SpannableStringBuilder()
//            var start = 0
//            val hh = "22"
//            var end = hh.length
//            builder.append(hh)
//            builder.setSpan(FrameSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//            val divider = ":"
//
//            builder.append(divider)
//            start = end + divider.length
//            val mm = "12"
//            end = start + mm.length
//            builder.append(mm)
//            builder.setSpan(FrameSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//            builder.append(divider)
//            start = end + divider.length
//            val ss = "12"
//            end = start + ss.length
//            builder.append(ss)
//            builder.setSpan(FrameSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//            binding.textView.text = builder
        }

    }
}