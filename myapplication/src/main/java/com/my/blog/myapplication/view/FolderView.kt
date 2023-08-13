package com.my.blog.myapplication.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class FolderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int
) : View(context, attrs, defaultStyleAttr) {

    private var mTextNormalSize = 0f
    private var mTextLargeSize = 0f
    private val mTextPaint =
        TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.LINEAR_TEXT_FLAG)

    private var mPointX = 0f
    private var mPointY = 0f

    private var mViewWidth = 0
    private var mViewHeight = 0

    private val mFullViewPath = Path()
    private val mFoldPath = Path()
    private val mFoldAndNextPath = Path()

    private var mRatio = Ratio.SHORT

    private var mDegrees = 0.0

    private var pageIndex = 0

    val mBitmaps = mutableListOf<Bitmap>()

    enum class Ratio {
        SHORT, LONG
    }

    init {
        mTextNormalSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
        mTextLargeSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics)

        mTextPaint.textAlign = Paint.Align.CENTER

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)

        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h

        mFullViewPath.addRect(0f, 0f, w.toFloat(), h.toFloat(), Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        if (mBitmaps.isEmpty()) {
            defaultShowDisplay(canvas)
            return
        }
        if (mPointX == 0f && mPointY == 0f) {
            canvas.drawBitmap(mBitmaps[0], 0f, 0f, null)
            return
        }
        mFoldPath.reset()
        mFoldAndNextPath.reset()

        val mK = mViewWidth - mPointX
        val mL = mViewHeight - mPointY
        val temp = Math.pow(mK.toDouble(), 2.0) + Math.pow(mL.toDouble(), 2.0)
        val sizeShort = temp / (2 * mK)
        val sizeLong = temp / (2 * mL)

        mFoldPath.moveTo(mPointX, mPointY)
        mFoldAndNextPath.moveTo(mPointX, mPointY)
        if (sizeLong > mViewHeight) {
            val topSizeLarge =
                (mViewWidth - mPointX) * (sizeLong - mViewHeight) / (sizeLong - (mViewHeight - mPointY))
            val topSizeShort = sizeShort * (sizeLong - mViewHeight) / sizeLong

            mFoldPath.lineTo((mViewWidth - topSizeLarge).toFloat(), 0f)
            mFoldPath.lineTo((mViewWidth - topSizeShort).toFloat(), 0f)
            mFoldPath.lineTo((mViewWidth - sizeShort).toFloat(), mViewHeight.toFloat())
            mFoldPath.close()

            mFoldAndNextPath.lineTo((mViewWidth - topSizeLarge).toFloat(), 0f)
            mFoldAndNextPath.lineTo(mViewWidth.toFloat(), 0f)
            mFoldPath.lineTo(mViewWidth.toFloat(), mViewHeight.toFloat())
            mFoldPath.lineTo((mViewWidth - sizeShort).toFloat(), mViewHeight.toFloat())
            mFoldPath.close()
        } else {
            mFoldPath.lineTo(mViewWidth.toFloat(), mViewHeight - sizeLong.toFloat())
            mFoldPath.lineTo((mViewWidth - sizeShort).toFloat(), mViewHeight.toFloat())
            mFoldPath.close()

            mFoldAndNextPath.lineTo(mViewWidth.toFloat(), mViewHeight - sizeLong.toFloat())
            mFoldAndNextPath.lineTo(mViewWidth.toFloat(), mViewHeight.toFloat())
            mFoldAndNextPath.lineTo((mViewWidth - sizeShort).toFloat(), mViewHeight.toFloat())
            mFoldAndNextPath.close()

        }

        if (sizeShort < sizeLong) {
            mRatio = Ratio.SHORT
            val sin = (mK - sizeShort) / sizeShort
            mDegrees = Math.asin(sin) / Math.PI * 180
        } else {
            mRatio = Ratio.LONG
            val cos = mK / sizeLong
            mDegrees = Math.acos(cos) / Math.PI * 180
        }

        drawBitmaps(canvas)

    }

    private fun drawBitmaps(canvas: Canvas) {
        if (pageIndex < 0) {
            pageIndex = 0
        }

        if (pageIndex >= mBitmaps.size) {
            pageIndex = mBitmaps.size - 1
            Toast.makeText(context, "this is the last page", Toast.LENGTH_SHORT).show()
            return
        }
        val first = mBitmaps[pageIndex]
        val second = if (pageIndex + 1 < mBitmaps.size) mBitmaps[pageIndex + 1] else null

        canvas.save()
        canvas.clipPath(mFullViewPath)
        canvas.clipPath(mFoldAndNextPath, Region.Op.DIFFERENCE)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.restore()



    }

    private fun defaultShowDisplay(canvas: Canvas) {
        mTextPaint.textSize = mTextLargeSize
        mTextPaint.color = Color.RED
        canvas.drawText("Waring!", width / 2f, height / 4f, mTextPaint)

        mTextPaint.textSize = mTextNormalSize
        mTextPaint.color = Color.BLACK
        canvas.drawText("You should init bitmaps first", width / 2f, height / 2f, mTextPaint)
    }
}