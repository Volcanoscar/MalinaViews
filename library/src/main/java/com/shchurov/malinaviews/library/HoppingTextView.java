package com.shchurov.malinaviews.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.LinkedList;

public class HoppingTextView extends TextView {

    private static final int ANIMATED_AT_ONCE = 3;

    private LinkedList<HoppingSpan> spansPool = new LinkedList<HoppingSpan>();
    private ValueAnimator animator = new ValueAnimator();

    public HoppingTextView(Context context) {
        super(context);
        init();
    }

    public HoppingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HoppingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        animator.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        animator.setInterpolator(new LinearInterpolator());
    }

    public void playHoppingAnimation() {
        CharSequence text = getText();
        final SpannableString string = text instanceof SpannableString ? (SpannableString) text : new SpannableString(text);
        for (int i = 0; i < string.length(); i++) {
            HoppingSpan span;
            if (spansPool.size() > i) {
                span = spansPool.get(i);
            } else {
                span = new HoppingSpan();
                spansPool.add(span);
            }
            span.setQueuePosition(i / (float) ANIMATED_AT_ONCE);
            string.setSpan(span, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(string, BufferType.SPANNABLE);
        animator.setFloatValues(0, (string.length() + ANIMATED_AT_ONCE - 1) / (float) ANIMATED_AT_ONCE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                for (int i = 0; i < string.length(); i++) {
                    spansPool.get(i).notifyAnimValueChanged(value);
                }
                invalidate();
            }
        });
        animator.start();
    }

    private class HoppingSpan extends MetricAffectingSpan {

        private float queuePosition;
        private float ratio = 0;

        public void setQueuePosition(float position) {
            queuePosition = position;
        }

        public void notifyAnimValueChanged(float value) {
            float shiftedValue = -queuePosition + value;
            if (shiftedValue >= 0 && shiftedValue <= 1) {
                ratio = (float) Math.sin(shiftedValue * Math.PI) / 2.5f;
            } else {
                ratio = 0;
            }
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            paint.baselineShift += paint.ascent() * ratio;
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.baselineShift += paint.ascent() * ratio;
        }

    }

}
