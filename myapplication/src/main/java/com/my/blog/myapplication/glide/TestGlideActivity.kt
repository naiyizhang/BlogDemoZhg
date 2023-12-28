package com.my.blog.myapplication.glide

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.my.blog.myapplication.R
import com.my.blog.myapplication.databinding.ActivityGlideViewsBinding
import okhttp3.OkHttpClient
import okio.Okio

class TestGlideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGlideViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val url =
                "https://img0.baidu.com/it/u=3896130905,2562016716&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800"
            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(binding.imageView)

            addView(binding)


        }

    }

    private fun addView(binding: ActivityGlideViewsBinding) {
        val constraintLayout = binding.root as ConstraintLayout
        val set = ConstraintSet()
        set.clone(constraintLayout)
//        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal, android.R.style.Widget_ProgressBar_Horizontal)
        progressBar.id = ViewCompat.generateViewId()
        progressBar.progress = 40
        constraintLayout.addView(progressBar)
        set.connect(progressBar.id, ConstraintSet.TOP, binding.button.id, ConstraintSet.BOTTOM)
        set.connect(progressBar.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(
            progressBar.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        set.constrainHeight(progressBar.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(progressBar.id, ConstraintSet.MATCH_CONSTRAINT)
        set.applyTo(constraintLayout)
    }
}