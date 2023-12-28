package com.my.blog.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.my.blog.myapplication.activity.TestAActivity
import com.my.blog.myapplication.activity.TestCActivity
import com.my.blog.myapplication.databinding.ActivityMainBinding
import com.my.blog.myapplication.glide.TestGlideActivity
import com.my.blog.myapplication.util.TestMyUtil
import com.my.blog.myapplication.viewmodel.MyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.foldview).setOnClickListener {
            startActivity(Intent(this, FoldViewActivity::class.java))
        }

        findViewById<Button>(R.id.erase).setOnClickListener {
            startActivity(Intent(this, EraseActivity::class.java))
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, TestDrawableActivity::class.java))
        }

        findViewById<Button>(R.id.scale).setOnClickListener {
            startActivity(Intent(this, TestScaleActivity::class.java))
        }

        findViewById<Button>(R.id.path).setOnClickListener {
            startActivity(Intent(this, TestPathActivity::class.java))
        }

        findViewById<Button>(R.id.gird).setOnClickListener {
            startActivity(Intent(this, TestGridActivity::class.java))
        }

        findViewById<Button>(R.id.floating).setOnClickListener {
            startActivity(Intent(this, TestFloatViewActivity::class.java))
        }

        findViewById<Button>(R.id.span).setOnClickListener {
            startActivity(Intent(this, TestSpanActivity::class.java))
        }
        val viewModel: MyViewModel by viewModels()
        findViewById<Button>(R.id.flow).setOnClickListener {
            viewModel.startTimer()

            sendNotification()
        }

        binding.activity.setOnClickListener {
            startActivity(Intent(this,TestAActivity::class.java))
        }

        binding.glide.setOnClickListener {
            startActivity(Intent(this,TestGlideActivity::class.java))

        }

        binding.testC.setOnClickListener {
            startActivity(Intent(this,TestCActivity::class.java))
        }

        binding.lottie.setOnClickListener {
            startActivity(Intent(this,LottieActivity::class.java))

        }


//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.stateFlow2.collect {
//                    Log.e(TAG, "onCreate: value is $it")
//                    findViewById<Button>(R.id.floating).text = it.toString()
//                }
//            }
//        }

        setFullscreen()

        val myGoods = TestMyUtil().setMyGoods(this,"one")
        findViewById<Button>(R.id.flow).text = myGoods

        Log.e(TAG, "onCreate: dpi ${resources.displayMetrics.densityDpi} ,destiny = ${resources.displayMetrics.density}")

        val bm = BitmapFactory.decodeResource(resources,R.drawable.avatar27)
        Log.e(TAG, "onCreate: width = ${bm.width} , height = ${bm.height}", )
    }

    companion object {
        private const val TAG = "MainActivity"
    }


    fun sendNotification() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            sendChatMsg()
        } else {
            val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    sendChatMsg()
                }
            }
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

    }

    private fun sendChatMsg() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("chat", "聊天通道", NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)

        } else {
        }
        val notification =
            NotificationCompat.Builder(this, "chat").setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle("this is a title")
                .setContentText("this is content")
                .setNumber(4)
                .setAutoCancel(true).build()
        notificationManager.notify(1, notification)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.e(TAG, "onWindowFocusChanged:$hasFocus " )
        if(hasFocus){
//            setFullscreen()
        }
    }

    private fun setFullscreen() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}


fun main() {


    runBlocking {
//        val flowA = flow {
//            delay(3000)
//            emit("A")
//            delay(1000)
//            emit("B")
//            emit("C")
//        }
//        val flowB = flow {
//            delay(2000)
//            emit(1)
//        }
//
//        flowA.zip(flowB){a,b->a+b
//        }.buffer().collect {
//            println(it)
//        }

        flow {
//            var times = 0
//            while (times<=1000) {
//                println(times)
//                emit(times++)
//                delay(100)
//            }
            emit(1)
            emit(2)
            delay(900)
            emit(3)
            delay(200)
            emit(4)
            delay(200)
            emit(5)

        }.debounce(500).collect {
            println("result value is $it")
        }
    }


}
