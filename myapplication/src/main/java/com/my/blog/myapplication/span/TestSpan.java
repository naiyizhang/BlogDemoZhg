//package com.my.blog.myapplication.span;
//
//import android.animation.ArgbEvaluator;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.graphics.Color;
//import android.text.Spanned;
//import android.util.Property;
//
//public class TestSpan {
//
//    private void animateColorSpan() {
//        MutableForegroundColorSpan span = new MutableForegroundColorSpan(255, mTextColor);
//        mSpans.add(span);
//
//        WordPosition wordPosition = getWordPosition(mBaconIpsum);
//        mBaconIpsumSpannableString.setSpan(span, wordPosition.start, wordPosition.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(span, MUTABLE_FOREGROUND_COLOR_SPAN_FC_PROPERTY, Color.BLACK, Color.RED);
//        objectAnimator.setEvaluator(new ArgbEvaluator());
//        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                //refresh
//                mText.setText(mBaconIpsumSpannableString);
//            }
//        });
//        objectAnimator.setInterpolator(mSmoothInterpolator);
//        objectAnimator.setDuration(600);
//        objectAnimator.start();
//    }
//
//    private static final Property<MutableForegroundColorSpan, Integer> MUTABLE_FOREGROUND_COLOR_SPAN_FC_PROPERTY =
//            new Property<MutableForegroundColorSpan, Integer>(Integer.class, "MUTABLE_FOREGROUND_COLOR_SPAN_FC_PROPERTY") {
//
//                @Override
//                public void set(MutableForegroundColorSpan alphaForegroundColorSpanGroup, Integer value) {
//                    alphaForegroundColorSpanGroup.setForegroundColor(value);
//                }
//
//                @Override
//                public Integer get(MutableForegroundColorSpan span) {
//                    return span.getForegroundColor();
//                }
//            };
//}
