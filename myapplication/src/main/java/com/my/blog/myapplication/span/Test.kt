//package com.my.blog.myapplication.span
//
//import android.animation.ArgbEvaluator
//import android.animation.ObjectAnimator
//import android.graphics.Color
//import android.os.Parcel
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.SpannableStringBuilder
//import android.text.TextPaint
//import android.text.style.ForegroundColorSpan
//import android.util.Property
//
//class MutableForegroundColorSpan : ForegroundColorSpan {
//    // 动画渐变值预设
//    companion object {
//        val MUTABLE_FOREGROUND_COLOR_SPAN_FC_PROPERTY: Property<MutableForegroundColorSpan, Int> =
//            object : Property<MutableForegroundColorSpan, Int>(
//                Int::class.java, "MUTABLE_FOREGROUND_COLOR_SPAN_FC_PROPERTY"
//            ) {
//                override operator fun set(span: MutableForegroundColorSpan, value: Int) {
//                    span.foregroundColor = value
//                }
//
//                override operator fun get(span: MutableForegroundColorSpan): Int {
//                    return span.foregroundColor
//                }
//            }
//    }
//
//    private var mAlpha = 255
//    private var mForegroundColor: Int
//
//    constructor(alpha: Int, color: Int) : super(color) {
//        mAlpha = alpha
//        mForegroundColor = color
//    }
//    // 原构造函数继承
//    constructor(src: Parcel): super(src) {
//
//        mForegroundColor = src.readInt()
//        mAlpha = src.readInt()
//    }
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        super.writeToParcel(dest, flags)
//        dest.writeInt(mForegroundColor)
//        dest.writeInt(mAlpha)
//    }
//    // 动画设置
//    fun animationColorChange(startColor: Int,endColor:Int) : ObjectAnimator {
//        val objectAnimator: ObjectAnimator = ObjectAnimator.ofInt(
//            this,
//            MutableForegroundColorSpan.MUTABLE_FOREGROUND_COLOR_SPAN_FC_PROPERTY,
//            startColor,
//            endColor
//        )
//        objectAnimator.setEvaluator(ArgbEvaluator())
//        objectAnimator.duration = 3000
//        return objectAnimator
//    }
//
//    // 透明度
//    fun setAlpha(alpha: Int) {
//        mAlpha = alpha
//    }
//
//    fun setForegroundColor(foregroundColor: Int) {
//        mForegroundColor = foregroundColor
//    }
//
//    // 更新画笔颜色
//    override fun updateDrawState(ds: TextPaint) {
//        ds.color = foregroundColor
//    }
//    // 获取文本颜色
//    override fun getForegroundColor(): Int {
//        return Color.argb(
//            mAlpha,
//            Color.red(mForegroundColor),
//            Color.green(mForegroundColor),
//            Color.blue(mForegroundColor)
//        )
//    }
//
//}
//
//// 样式设置 监听动画回调重新设置样式从而刷新文本
//private var mutableForegroundColorSpan =
//    MutableForegroundColorSpan(255, Color.BLACK)
//fun start() {
//    mutableForegroundColorSpan.animationColorChange(
//        Color.BLACK,
//        Color.RED
//    ).run {
//        addUpdateListener {
//            mutableForegroundColorView.text = animationColor()
//        }
//        start()
//    }
//}
//
//// 文本样式配置
//private fun animationColor(): SpannableStringBuilder {
//    var spannableString = SpannableStringBuilder("")
//    spannableString.also { span ->
//        span.append(SpannableString("xxxxMutableForegroundColorSpanyyyy").also {
//            it.setSpan(
//                mutableForegroundColorSpan,
//                4,
//                "MutableForegroundColorSpan".length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        })
//    }
//    return spannableString
//}