package com.my.blog.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import android.text.style.ReplacementSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.my.blog.myapplication.databinding.ActivityTestFloatViewBinding


class TestFloatViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestFloatViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val binding = ActivityTestFloatViewBinding.bind(findViewById(R.id.root))
        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.avatar27)
        val imageView = findViewById<ImageView>(R.id.imageview)
//        binding.imageview.setImageBitmap(bitmap)
        findViewById<Button>(R.id.add_float).setOnClickListener {
            val drawable = imageView.drawable as BitmapDrawable
            val viewByteCount = drawable.bitmap.byteCount
            val bitmapCount = bitmap.byteCount
            val calcCount = bitmap.width*bitmap.height *4
            Log.e(Companion.TAG, "onCreate: ViewWidth = ${imageView.width} , ViewHeight = ${imageView.height} , viewSize = $viewByteCount viewBitmap width = ${drawable.bitmap.width} , viewBitmap height = ${drawable.bitmap.height} , drawable width ${drawable.intrinsicWidth} , drawable height is ${drawable.intrinsicHeight}" )
            Log.e(TAG, "onCreate: bitmap.scaleWidth = ${drawable.bitmap.getScaledWidth(resources.displayMetrics.densityDpi)} , scaleHeight = ${drawable.bitmap.getScaledHeight(resources.displayMetrics.densityDpi)}" )
            Log.e(TAG, "onCreate: bitmap width is ${bitmap.width} , bitmap height is ${bitmap.height} , bitmap bytecount $bitmapCount" )
            Log.e(TAG, "onCreate: calcCount is $calcCount")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                    )
                    startActivityForResult(intent, 1)
                    return@setOnClickListener
                }
            }

            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels
            val textView = TextView(this@TestFloatViewActivity)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            textView.text = "add new float view"
            textView.gravity = Gravity.CENTER
            textView.setBackgroundColor(Color.GREEN)
            val params = WindowManager.LayoutParams()
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE
            }
            params.format = PixelFormat.RGBA_8888
            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            params.width = 500
            params.height = 100
            params.gravity = Gravity.LEFT or Gravity.TOP
//            params.x = 500
//            params.y = -90
//            params.x = screenWidth
//            params.y = screenHeight / 2
            windowManager.addView(textView, params)

        }

        binding.textView.text = addFixedSpaceInMiddle("This is a Txt",60,this)
        binding.textView.setBackgroundColor(Color.parseColor("#33FF0000"))
    }

    fun addLeadingMarginSpan(text: String, marginSizeInPixels: Int): SpannableString? {
        // 创建一个SpannableString对象
        val spannableString = SpannableString(text)

        // 创建一个LeadingMarginSpan.Standard对象，用于设置左右边距
        val marginSpan = LeadingMarginSpan.Standard(marginSizeInPixels, marginSizeInPixels)

        // 将LeadingMarginSpan应用于SpannableString
        spannableString.setSpan(marginSpan, 0, spannableString.length, 0)
        return spannableString
    }

    fun addFixedSpaceInMiddle(
        text: String,
        spaceInDp: Int,
        context: Context
    ): SpannableStringBuilder? {
        // 将dp转换为像素
        val spaceInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            spaceInDp.toFloat(),
            context.resources.displayMetrics
        ).toInt()

        // 创建一个SpannableStringBuilder对象
        val builder = SpannableStringBuilder(text)

        // 计算需要插入空格的位置
        val insertionPoint = text.length / 2

        // 创建一个自定义的ReplacementSpan，用于插入空格
        val spaceSpan: ReplacementSpan = object : ReplacementSpan() {
            override fun getSize(
                paint: Paint,
                text: CharSequence,
                start: Int,
                end: Int,
                fm: Paint.FontMetricsInt?
            ): Int {
                return spaceInPixels // 返回空格的宽度
            }

            override fun draw(
                canvas: Canvas,
                text: CharSequence,
                start: Int,
                end: Int,
                x: Float,
                top: Int,
                y: Int,
                bottom: Int,
                paint: Paint
            ) {
                // 在指定位置绘制空格
                canvas.drawText(" ", x, y.toFloat(), paint)
            }
        }

        // 将自定义的ReplacementSpan应用到SpannableStringBuilder
        builder.setSpan(
            spaceSpan,
            insertionPoint,
            insertionPoint + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        return super.onMenuOpened(featureId, menu)
    }
    companion object {
        private const val TAG = "TestFloatViewActivity"
    }
}