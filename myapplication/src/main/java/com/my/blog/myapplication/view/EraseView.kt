package com.my.blog.myapplication.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.my.blog.myapplication.R

class EraseView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val mPath = Path()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private lateinit var mFgBitmap: Bitmap
    private var mBgBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_little_logo)
    private lateinit var mCanvas: Canvas

    private var mPreX = 0f
    private var mPreY = 0f

    private val mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 50f
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.color = Color.WHITE
        mPaint.xfermode = PorterDuffXfermode(Mode.DST_OUT)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBgBitmap = Bitmap.createScaledBitmap(mBgBitmap, w, h, true)
        mFgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mFgBitmap)
        mCanvas.drawColor(Color.GRAY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPath.reset()
                mPreX = event.x
                mPreY = event.y
                mPath.moveTo(mPreX, mPreY)
            }

            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(event.x - mPreX) > mTouchSlop || Math.abs(event.y - mPreY) > mTouchSlop) {
                    mPath.quadTo(mPreX, mPreY, (mPreX + event.x) / 2, (mPreY + event.y) / 2)
                    mPreX = event.x
                    mPreY = event.y

                    mCanvas.drawPath(mPath, mPaint)
                    invalidate()
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {

        canvas ?: return
        canvas.drawBitmap(mBgBitmap, 0f, 0f, null)
//        canvas.drawBitmap(mFgBitmap, 0f, 0f, null)
        val sc = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        canvas.drawColor(Color.GRAY)
        canvas.drawPath(mPath, mPaint)
        canvas.restoreToCount(sc)
    }
}