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
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class FolderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleDef: Int = 0
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

    private val mPathTrap = Path()
    private val mBottomPathSemicircle = Path()
    private val mTopPathSemicircle = Path()

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

    var bitmaps = listOf<Bitmap>()

    private val mVerts = FloatArray((SUB_HEIGHT + 1) * (SUB_WIDTH + 1) * 2)

    private var mSubMinWidth = 0f
    private var mSubMinHeight = 0f

    companion object {
        private const val TAG = "FolderView"
        const val VALUE_ADD_FACTOR = 1 / 500F
        const val VALUE_BUFFER_AREA = 1 / 20F

        const val AUTO_SLIDE_LEFT = 1 / 8f
        const val AUTO_SLIDE_RIGHT = 1 / 4F

        const val AUTO_SLIDE_LEFT_SPEED = 1 / 25F
        const val AUTO_SLIDE_RIGHT_SPEED = 1 / 100F

        const val CURVATURE = 1 / 4F

        const val SUB_WIDTH = 19
        const val SUB_HEIGHT = 19
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
            mPageIndex++
            invalidate()
        } else if (mSlide == Slide.LEFT && mPointX >= -mViewWidth) {
            mPointX -= mAutoLeftSpeed
            mPointY =
                mStartY + (mViewHeight - mStartY) * (mStartX - mPointX) / (mStartX + mViewWidth)
            invalidate()
            slideNextFrame()
        } else if (mSlide == Slide.RIGHT && mPointX <= mViewWidth) {
            mPointX += mAutoRightSpeed
            mPointY =
                mStartY + (mViewHeight - mStartY) * (mPointX - mStartX) / (mViewWidth - mStartX)


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

        mSubMinWidth = mViewWidth / (SUB_WIDTH)
        mSubMinHeight = mViewHeight / (SUB_HEIGHT)
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
                    mSlide = Slide.RIGHT
                    justSlide(x, y)
                } else if (x < mAutoSlideLeft) {
                    mSlide = Slide.LEFT
                    justSlide(x, y)
                }
            }
        }
        return true
    }

    private fun downAndMove(x: Float, y: Float) {
        if (!mIsLastPage) {
            mPointX = x
            mPointY = y
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas ?: return super.onDraw(canvas)
        if (bitmaps.isEmpty()) {
            showDefaultDisplay(canvas)
            return
        }

        if ((mPointX == 0f && mPointY == 0f)) {
            canvas.drawBitmap(bitmaps[0], 0f, 0f, null)
            return
        }

        if (mPointY == mViewHeight) {
            canvas.drawBitmap(bitmaps[mPageIndex], 0f, 0f, null)
            return
        }

        canvas.drawColor(Color.WHITE)

        mFoldPath.reset()
        mFoldAndNextPath.reset()
        mPathTrap.reset()
        mBottomPathSemicircle.reset()
        mTopPathSemicircle.reset()

        if (!mFoldTouchRegion.contains(mPointX.toInt(), mPointY.toInt())) {
            mPointY = (mViewHeight - Math.sqrt(
                Math.pow(
                    mViewWidth.toDouble(),
                    2.0
                ) - Math.pow(mPointX.toDouble(), 2.0)
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

        if (sizeLong > mViewHeight) {
            val tempOverSize = sizeLong - mViewHeight
            val topLargeSize = mK * tempOverSize / (sizeLong - mL)
            val topSmallSize = sizeShort * tempOverSize / sizeLong

            val largeXCoordinate = mViewWidth - topLargeSize
            val smallXCoordinate = mViewWidth - topSmallSize

            val bottomXCoordinate = mViewWidth - sizeShort

            val startX = bottomXCoordinate - sizeShort * CURVATURE
            val startY = mViewHeight

            val controlX = bottomXCoordinate
            val controlY = mViewHeight

            val endX = bottomXCoordinate - (bottomXCoordinate - mPointX) * CURVATURE
            val endY = mPointY + (1 - CURVATURE) * mL

            val bezierPeakX = 0.25 * startX + 0.5 * controlX + 0.25 * endX
            val bezierPeakY = 0.25 * startY + 0.5 * controlY + 0.25 * endY

            mFoldPath.moveTo(startX.toFloat(), startY)
            mFoldPath.quadTo(controlX.toFloat(), controlY, endX.toFloat(), endY)
            mFoldPath.lineTo(mPointX, mPointY)
            mFoldPath.lineTo(largeXCoordinate.toFloat(), 0f)
            mFoldPath.lineTo(smallXCoordinate.toFloat(), 0f)
//            mFoldPath.close()

            mFoldAndNextPath.moveTo(startX.toFloat(), startY)
            mFoldAndNextPath.quadTo(controlX.toFloat(), controlY, endX.toFloat(), endY)
            mFoldAndNextPath.lineTo(mPointX, mPointY)
            mFoldAndNextPath.lineTo(largeXCoordinate.toFloat(), 0f)
            mFoldAndNextPath.lineTo(mViewWidth, 0f)
            mFoldAndNextPath.lineTo(mViewWidth, mViewHeight)
            mFoldAndNextPath.close()

            mPathTrap.moveTo(startX.toFloat(), startY)
            mPathTrap.lineTo(smallXCoordinate.toFloat(), 0f)
            mPathTrap.lineTo(bezierPeakX.toFloat(), bezierPeakY.toFloat())
            mPathTrap.close()

            mBottomPathSemicircle.moveTo(startX.toFloat(), startY)
            mBottomPathSemicircle.quadTo(controlX.toFloat(), controlY, endX.toFloat(), endY)
            mBottomPathSemicircle.close()

            val bottomStartIndex = Math.round(bottomXCoordinate / mSubMinWidth) - 1
            val bottomEndIndex =
                Math.round((bottomXCoordinate + CURVATURE * sizeShort) / mSubMinWidth) + 1
            val offsetShort = CURVATURE / 2F * sizeShort
            var multiOffsetShort = 1.0f
            var index = 0
            for (y in 0..SUB_HEIGHT) {
                var fy = mViewHeight / SUB_HEIGHT * y
                for (x in 0..SUB_WIDTH) {
                    var fx = mViewWidth / SUB_WIDTH * x

                    if (y == SUB_HEIGHT) {
                        if (x in bottomStartIndex..bottomEndIndex) {
                            fy =
                                (mViewHeight / SUB_HEIGHT * y + offsetShort * multiOffsetShort).toFloat()
                            multiOffsetShort *= 1.5F
                        }
                    }
                    mVerts[index * 2 + 0] = fx
                    mVerts[index * 2 + 1] = fy
                    index++
                }
            }
        } else {
            val topYCoordinate = mViewHeight - sizeLong
            val bottomXCoordinate = mViewWidth - sizeShort

            var bottomStartX = bottomXCoordinate - sizeShort * CURVATURE
            val bottomStartY = mViewHeight

            val bottomControlX = bottomXCoordinate
            val bottomControlY = mViewHeight

            val bottomEndX = bottomXCoordinate - (bottomXCoordinate - mPointX) * CURVATURE
            val bottomEndY = mPointY + (1 - CURVATURE) * mL

            var bottomBezierPeakX = 0.25 * bottomStartX + 0.5 * bottomControlX + 0.25 * bottomEndX
            var bottomBezierPeakY = 0.25 * bottomStartY + 0.5 * bottomControlY + 0.25 * bottomEndY

            val topStartX = mViewWidth
            var topStartY = topYCoordinate - sizeLong * CURVATURE

            val topControlX = mViewWidth
            val topControlY = topYCoordinate

//            val topEndX = mViewWidth - mK * CURVATURE
            val topEndX = mPointX + (1 - CURVATURE) * mK
            val topEndY = mPointY - (1 - CURVATURE) * (sizeLong - mL)

            var topBezierPeakX = 0.25 * topStartX + 0.5 * topControlX + 0.25 * topEndX
            var topBezierPeakY = 0.25 * topStartY + 0.5 * topControlY + 0.25 * topEndY

            if (topStartY < 0) {
                topStartY = 0.0
            }
            if (bottomStartX < 0f) {
                bottomStartX = 0.0
            }
            val shortLengthMax = sizeShort * CURVATURE
            if (bottomXCoordinate > -mValueAdd && bottomXCoordinate < shortLengthMax - mValueAdd) {
                val f = bottomXCoordinate / shortLengthMax
                val t = 0.5 * f
                val bezierTemp = 1 - t
                val bezierTemp1 = bezierTemp * bezierTemp
                val bezierTemp2 = 2 * t * bezierTemp
                val bezierTemp3 = t * t
                bottomBezierPeakX =
                    bezierTemp1 * bottomStartX + bezierTemp2 * bottomControlX + bezierTemp3 * bottomEndX
                bottomBezierPeakY =
                    bezierTemp1 * bottomStartY + bezierTemp2 * bottomControlY + bezierTemp3 * bottomEndY
            }

            val longLengthMax = sizeLong * CURVATURE
            if (topYCoordinate > -mValueAdd && topYCoordinate < longLengthMax - mValueAdd) {
                val f = topYCoordinate / longLengthMax
                val t = 0.5 * f
                val bezierTemp = 1 - t
                val bezierTemp1 = bezierTemp * bezierTemp
                val bezierTemp2 = 2 * t * bezierTemp
                val bezierTemp3 = t * t
                topBezierPeakX =
                    bezierTemp1 * topStartX + bezierTemp2 * topControlX + bezierTemp3 * topEndX
                topBezierPeakY =
                    bezierTemp1 * topStartY + bezierTemp2 * topControlY + bezierTemp3 * topEndY
            }

            mFoldPath.moveTo(bottomStartX.toFloat(), bottomStartY)
            mFoldPath.quadTo(
                bottomControlX.toFloat(), bottomControlY,
                bottomEndX.toFloat(), bottomEndY
            )
            mFoldPath.lineTo(mPointX, mPointY)
            mFoldPath.lineTo(topEndX, topEndY.toFloat())
            mFoldPath.quadTo(topControlX, topControlY.toFloat(), topStartX, topStartY.toFloat())
//            mFoldPath.close()

            mFoldAndNextPath.moveTo(bottomStartX.toFloat(), bottomStartY)
            mFoldAndNextPath.quadTo(
                bottomControlX.toFloat(), bottomControlY,
                bottomEndX.toFloat(), bottomEndY
            )
            mFoldAndNextPath.lineTo(mPointX, mPointY)
            mFoldAndNextPath.lineTo(topEndX, topEndY.toFloat())
            mFoldAndNextPath.quadTo(
                topControlX,
                topControlY.toFloat(),
                topStartX,
                topStartY.toFloat()
            )
            mFoldAndNextPath.lineTo(mViewWidth, mViewHeight)
            mFoldAndNextPath.close()

            mPathTrap.moveTo(bottomStartX.toFloat(), bottomStartY)
            mPathTrap.lineTo(topStartX, topStartY.toFloat())
            mPathTrap.lineTo(topBezierPeakX.toFloat(), topBezierPeakY.toFloat())
            mPathTrap.lineTo(bottomBezierPeakX.toFloat(), bottomBezierPeakY.toFloat())
            mPathTrap.close()

            mBottomPathSemicircle.moveTo(bottomStartX.toFloat(), bottomStartY)
            mBottomPathSemicircle.quadTo(
                bottomControlX.toFloat(), bottomControlY,
                bottomEndX.toFloat(), bottomEndY
            )
            mBottomPathSemicircle.close()

            mTopPathSemicircle.moveTo(topEndX, topEndY.toFloat())
            mTopPathSemicircle.quadTo(
                topControlX, topControlY.toFloat(), topStartX,
                topStartY.toFloat()
            )
            mTopPathSemicircle.close()

            mBottomPathSemicircle.op(mTopPathSemicircle, Path.Op.UNION)


            val bottomStartIndex = Math.round(bottomXCoordinate / mSubMinWidth) - 1
            val bottomEndIndex =
                Math.round((bottomXCoordinate + CURVATURE * sizeShort) / mSubMinWidth) + 1

            val topStartIndex = Math.round(topYCoordinate / mSubMinHeight) - 1
            val topEndIndex =
                Math.round((topYCoordinate + CURVATURE * sizeLong) / mSubMinHeight) + 1

            val offsetLong = CURVATURE / 2F * sizeLong
            var multiOffsetLong = 1.0f
            val offsetShort = CURVATURE / 2F * sizeShort
            var multiOffsetShort = 1.0f
            var index = 0
            for (y in 0..SUB_HEIGHT) {
                var fy = mViewHeight / SUB_HEIGHT * y
                for (x in 0..SUB_WIDTH) {
                    var fx = mViewWidth / SUB_WIDTH * x
                    if (x == SUB_WIDTH) {
                        if (y in topStartIndex..topEndIndex) {
                            fx =
                                (mViewWidth / SUB_WIDTH * x + offsetLong * multiOffsetLong).toFloat()
                            multiOffsetLong *= 1.5F
                        }
                    }

                    if (y == SUB_HEIGHT) {
                        if (x in bottomStartIndex..bottomEndIndex) {
                            fy =
                                (mViewHeight / SUB_HEIGHT * y + offsetShort * multiOffsetShort).toFloat()
                            multiOffsetShort *= 1.5F
                        }
                    }
                    mVerts[index * 2 + 0] = fx
                    mVerts[index * 2 + 1] = fy
                    index++
                }
            }
        }

        mFoldPath.op(mPathTrap, Path.Op.UNION)
        mFoldPath.op(mBottomPathSemicircle, Path.Op.DIFFERENCE)

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
        if (mPageIndex < 0) {
            mPageIndex = 0
        }
        if (mPageIndex >= bitmaps.size) {
            mIsLastPage = true
            mPageIndex == bitmaps.size - 1
            Toast.makeText(context, "this is the last page", Toast.LENGTH_SHORT).show()
            return
        }

        val firstBitmap = bitmaps[mPageIndex]
        val secondBitmap = if (mPageIndex + 1 < bitmaps.size) bitmaps[mPageIndex + 1] else null
        if (secondBitmap == null) {
            Toast.makeText(context, "this is the last page", Toast.LENGTH_SHORT).show()
            canvas.drawBitmap(firstBitmap, 0f, 0f, null)
            return
        }
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
//        canvas.drawBitmap(firstBitmap, 0f, 0f, null)
        canvas.drawBitmapMesh(firstBitmap, SUB_WIDTH, SUB_HEIGHT, mVerts, 0, null, 0, null)
        canvas.restore()
        if (secondBitmap == null) {
            mIsLastPage = true
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
        for (bm in bitmaps) {
            list.add(Bitmap.createScaledBitmap(bm, mViewWidth.toInt(), mViewHeight.toInt(), true))
        }
        bitmaps = list
    }

    private fun computeRegion(path: Path): Region {
        val region = Region()
        val rectF = RectF()
        path.computeBounds(rectF, true)
        region.setPath(
            path, Region(
                rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(),
                rectF.bottom.toInt()
            )
        )
        return region
    }
}