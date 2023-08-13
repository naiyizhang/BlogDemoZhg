package com.my.blog.myapplication.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class FolderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleDef: Int
) : View(context, attrs, defStyleDef) {

    private var mLargerTextSize = 0f
    private var mSmallTextSize = 0f
    private val mTextPaint =
        TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.LINEAR_TEXT_FLAG)

    private var mViewWidth = 0f
    private var mViewHeight = 0f

    private var mPointX = 0f
    private var mPointY = 0f

    private val mFoldPath = Path()
    private val mFoldAndNextPath = Path()

    private var mFoldTouchRegion = Region()

    private var mValueAdd = 0f
    private var mBufferArea = 0f

    private var mIsSlide = false

    private var mRatio = Ratio.SHORT
    private var mDegrees = 0.0

    private val mCurrentPath = Path()

    private var mPageIndex = 0
    private var mIsLastPage = false

    private var mAutoSlideLeft = 0f
    private var mAutoSlideRight = 0f
    private var mAutoSlidBottom = 0f

    private var mAutoLeftSpeed = 0f
    private var mAutoRightSpeed = 0f

    private var mStartX = 0f
    private var mStartY = 0f

    private var mSlide = Slide.LEFT

    var mBitmaps = listOf<Bitmap>()

    companion object {
        const val VALUE_ADD_FACTOR = 1 / 500F
        const val VALUE_BUFFER_AREA = 1 / 20F

        const val AUTO_SLIDE_LEFT = 1 / 8f
        const val AUTO_SLIDE_RIGHT = 1 / 4F

        const val AUTO_SLIDE_LEFT_SPEED = 1 / 25F
        const val AUTO_SLIDE_RIGHT_SPEED = 1 / 100F
    }

    enum class Ratio {
        SHORT, LONG
    }

    enum class Slide {
        LEFT, RIGHT
    }

    private val mSlideHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            slide()
        }
    }

    private fun justSlide(x: Float, y: Float) {
        mIsSlide = true
        mStartX = x
        mStartY = y
        slide()
    }

    private fun slide() {
        if (!mIsLastPage && (mPointX - mAutoLeftSpeed <= -mViewWidth)) {
            mPointX = -mViewWidth
            mPointY = mViewHeight
            mPageIndex--
            invalidate()
        } else if (mSlide == Slide.LEFT && mPointX >= -mViewWidth) {
            mPointX -= mAutoLeftSpeed
            mPointY =
                mStartY + (mViewHeight - mStartY) * (mStartX + mViewWidth) / (mStartX - mPointX)
            invalidate()
            slideNextFrame()
        } else if (mSlide == Slide.RIGHT && mPointX <= mViewWidth) {
            mPointX += mAutoRightSpeed
            mPointY =
                mStartY + (mViewHeight - mPointY) * (mPointX - mStartX) / (mViewWidth - mStartX)
            invalidate()
            slideNextFrame()
        }
    }

    private fun slideNextFrame() {
        mSlideHandler.removeMessages(0)
        mSlideHandler.sendEmptyMessageDelayed(0, 25)
    }

    init {
        mLargerTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics)
        mSmallTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics)
        mTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mViewWidth = w.toFloat()
        mViewHeight = h.toFloat()

        mFoldTouchRegion = computeFoldTouchRegion()

        mValueAdd = mViewHeight * VALUE_ADD_FACTOR
        mBufferArea = mViewHeight * VALUE_BUFFER_AREA

        mCurrentPath.addRect(0f, 0f, mViewWidth, mViewHeight, Path.Direction.CW)

        mAutoSlideLeft = mViewWidth * AUTO_SLIDE_LEFT
        mAutoSlideRight = mViewWidth * AUTO_SLIDE_RIGHT
        mAutoSlidBottom = mViewHeight * AUTO_SLIDE_RIGHT

        mAutoLeftSpeed = mViewWidth * AUTO_SLIDE_LEFT_SPEED
        mAutoRightSpeed = mViewWidth * AUTO_SLIDE_RIGHT_SPEED

        initBitmaps()
    }

    private fun computeFoldTouchRegion(): Region {
        val region = Region()
        val path = Path()
        val rectF = RectF()
        path.addCircle(0f, mViewHeight, mViewWidth, Path.Direction.CW)
        path.computeBounds(rectF, true)
        region.setPath(
            path, Region(
                rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(),
                rectF.bottom.toInt()
            )
        )
        return region
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        val x = event.x
        val y = event.y
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (x <= mAutoSlideLeft) {
                    mPointX = x
                    mPointY = y
                    mPageIndex--
                    invalidate()
                } else {
                    downAndMove(x, y)
                }
            }

            MotionEvent.ACTION_MOVE -> downAndMove(x, y)
            MotionEvent.ACTION_UP -> {
                if (x > mAutoSlideRight && y > mAutoSlidBottom) {
                    justSlide(x, y)
                } else if (x < mAutoSlideLeft) {
                    justSlide(x, y)
                }
            }
        }
        return true
    }

    private fun downAndMove(x: Float, y: Float) {
        if (mIsLastPage) {
            mPointX = x
            mPointY = y
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return super.onDraw(canvas)
        if (mBitmaps.isEmpty()) {
            showDefaultDisplay(canvas)
            return
        }

        if ((mPointX == 0f && mPointY == 0f) || mPointY == mViewHeight) {
            canvas.drawBitmap(mBitmaps[0], 0f, 0f, null)
            return
        }


        canvas.drawColor(Color.WHITE)

        mFoldPath.reset()
        mFoldAndNextPath.reset()

        if (!mFoldTouchRegion.contains(mPointX.toInt(), mPointY.toInt())) {
            mPointY = (mViewHeight - Math.sqrt(
                Math.pow(
                    mViewWidth.toDouble(),
                    2.0
                ) + mPointX.toDouble()
            )).toFloat()
            mPointY += mValueAdd
        }

        val area = mViewHeight - mBufferArea
        if (!mIsSlide && mPointY > area) {
            mPointY = area
        }

        val mK = mViewWidth - mPointX
        val mL = mViewHeight - mPointY
        val temp = Math.pow(mK.toDouble(), 2.0) + Math.pow(mL.toDouble(), 2.0)

        val sizeShort = temp / (2 * mK)
        val sizeLong = temp / (2 * mL)

        mFoldPath.moveTo(mPointX, mPointY)
        mFoldAndNextPath.moveTo(mPointX, mPointY)
        if (sizeLong > mViewHeight) {
            val tempOverSize = sizeLong - mViewHeight
            val topLargeSize = mK * tempOverSize / (sizeLong - mL)
            val topSmallSize = sizeShort * tempOverSize / sizeLong

            val largeXCoordinate = mViewWidth - topLargeSize
            val smallXCoordinate = mViewWidth - topSmallSize
            val bottomXCoordinate = mViewWidth - sizeShort
            mFoldPath.lineTo(largeXCoordinate.toFloat(), 0f)
            mFoldPath.lineTo(smallXCoordinate.toFloat(), 0f)
            mFoldPath.lineTo(bottomXCoordinate.toFloat(), mViewHeight)
            mFoldPath.close()

            mFoldAndNextPath.lineTo(largeXCoordinate.toFloat(), 0f)
            mFoldAndNextPath.lineTo(mViewWidth, 0f)
            mFoldAndNextPath.lineTo(mViewWidth, mViewHeight)
            mFoldAndNextPath.lineTo(bottomXCoordinate.toFloat(), mViewHeight)
            mFoldAndNextPath.close()
        } else {
            val topYCoordinate = mViewHeight - sizeLong
            val bottomXCoordinate = mViewWidth - sizeShort

            mFoldPath.lineTo(mViewWidth, topYCoordinate.toFloat())
            mFoldPath.lineTo(bottomXCoordinate.toFloat(), mViewHeight)
            mFoldPath.close()

            mFoldAndNextPath.lineTo(mViewWidth, topYCoordinate.toFloat())
            mFoldAndNextPath.lineTo(mViewWidth, mViewHeight)
            mFoldAndNextPath.lineTo(bottomXCoordinate.toFloat(), mViewHeight)
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
        if (mPageIndex >= mBitmaps.size) {
            mIsLastPage = true
            Toast.makeText(context, "this is the last page", Toast.LENGTH_SHORT).show()
            return
        }
        val firstBitmap = mBitmaps[mPageIndex]
        val secondBitmap = if (mPageIndex + 1 < mBitmaps.size) mBitmaps[mPageIndex + 1] else null

        canvas.save()
        canvas.clipPath(mCurrentPath)
        canvas.clipPath(mFoldAndNextPath, Region.Op.DIFFERENCE)
        canvas.drawBitmap(firstBitmap, 0f, 0f, null)
        canvas.restore()

        canvas.save()
        canvas.clipPath(mFoldPath)
        canvas.translate(mPointX, mPointY)
        if (mRatio == Ratio.SHORT) {
            canvas.rotate((90 - mDegrees).toFloat())
            canvas.translate(0f, -mViewHeight)
            canvas.scale(-1f, 1f)
            canvas.translate(-mViewWidth, 0f)
        } else {
            canvas.rotate((-(90 - mDegrees)).toFloat())
            canvas.translate(-mViewWidth, 0f)
            canvas.scale(1f, -1f)
            canvas.translate(0f, -mViewHeight)
        }
        canvas.drawBitmap(firstBitmap, 0f, 0f, null)
        canvas.restore()
        if (secondBitmap == null) {
            Toast.makeText(context, "this is the last page", Toast.LENGTH_SHORT).show()
        } else {
            canvas.save()
            canvas.clipPath(mFoldAndNextPath)
            canvas.clipPath(mFoldPath, Region.Op.DIFFERENCE)
            canvas.drawBitmap(secondBitmap, 0f, 0f, null)
        }
    }

    private fun showDefaultDisplay(canvas: Canvas) {
        mTextPaint.color = Color.RED
        mTextPaint.textSize = mLargerTextSize
        canvas.drawText("Waring!", mViewWidth / 2, mViewHeight / 4, mTextPaint)

        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = mSmallTextSize
        canvas.drawText("Waring!", mViewWidth / 2, mViewHeight / 2, mTextPaint)
    }

    private fun initBitmaps() {
        val list = mutableListOf<Bitmap>()
        for (bm in mBitmaps) {
            list.add(Bitmap.createScaledBitmap(bm, mViewWidth.toInt(), mViewHeight.toInt(), true))
        }
        mBitmaps = list
    }
}