package com.my.blog.myapplication.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ComponentCallbacks2
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.my.blog.myapplication.R
import com.my.blog.myapplication.databinding.ActivityTestAactivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.io.FileDescriptor
import java.lang.RuntimeException
import java.net.URL
import kotlin.concurrent.thread

class TestAActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestAactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
//            startActivity(Intent(this, TestBActivity::class.java).apply {
////                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            })
            startAnimation(binding)

        }
        Log.e(Companion.TAG, "TestA.onCreate: ")

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_5)
        Log.e(TAG, "onCreate: bm = ${bitmap.byteCount}")

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        thread {
            val url = URL("https://www.baidu.com/")
            val connection = url.openConnection()
            Log.e(TAG, "onCreate: connection class = " + connection.javaClass.name)
        }

    }

    private fun startAnimation(binding: ActivityTestAactivityBinding) {
        val anim1 = ObjectAnimator.ofFloat(binding.button, "translationX", -500f, 0f)
        anim1.duration = 3000
        val anim2 = ObjectAnimator.ofFloat(binding.button, "rotation", 0f, 360f)
        anim2.duration = 3000
        val anim3 = ObjectAnimator.ofFloat(binding.button, "alpha", 1f, 0f, 1f)
        anim3.duration = 3000
        val set = AnimatorSet()
//        set.play(anim1).before(anim2).before(anim3)
        set.play(anim2).before(anim3).after(anim1)
        set.start()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {

        }
        Log.e(TAG, "onTrimMemory: level = " + level)
    }

    companion object {
        private const val TAG = "TestAActivity"
    }
}

fun main() {
    runBlocking {
        val flow = flow {
            emit(1)
            emit(2)
            delay(600)
            emit(3)
        }
        flow.debounce(500).collect{
            println(it)
        }

    }
}