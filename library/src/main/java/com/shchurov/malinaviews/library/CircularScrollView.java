package com.shchurov.malinaviews.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.LinkedList;
import java.util.List;

public class CircularScrollView extends ViewGroup {

    private static final float ANGLE_STEP = (float) (Math.PI / 4);
    private static final int INNER_PADDING_DP = 40;

    private int innerPadding;
    private View centralView;
    private List<View> itemViews = new LinkedList<View>();
    private Point center = new Point();
    private float scrollAngleOffset;
    private int touchSlop;
    private float visibilityLimitAngle;
    private boolean laidOut;

    public CircularScrollView(Context context) {
        super(context);
        init();
    }

    public CircularScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        innerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INNER_PADDING_DP, getContext().getResources().getDisplayMetrics());

    }

    public void setCentralView(View view) {
        if (centralView != null) {
            removeView(centralView);
        }
        centralView = view;
        addView(centralView);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (child != centralView) {
            int m = index == -1 ? itemViews.size() : index;
            itemViews.add(m, child);
        }
        if (!laidOut) {
            scrollAngleOffset = -(itemViews.size() - 1) * ANGLE_STEP / 2;
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int maxItemViewSize = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            int widthSpec = defaultSpec;
            int heightSpec = defaultSpec;
            if (lp.width != LayoutParams.MATCH_PARENT && lp.width != LayoutParams.WRAP_CONTENT) {
                widthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            }
            if (lp.height != LayoutParams.MATCH_PARENT && lp.height != LayoutParams.WRAP_CONTENT) {
                heightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            }
            child.measure(widthSpec, heightSpec);
            if (child != centralView) {
                if (maxItemViewSize < child.getMeasuredWidth()) {
                    maxItemViewSize = child.getMeasuredWidth();
                }
                if (maxItemViewSize < child.getMeasuredHeight()) {
                    maxItemViewSize = child.getMeasuredHeight();
                }
            }
        }
        float halfCentralMeasuredWidth = centralView.getMeasuredWidth() * 0.5f;
        center.x = (int) halfCentralMeasuredWidth;
        float halfSize = maxItemViewSize * 0.5f;
        float hypotenuse = halfCentralMeasuredWidth + innerPadding + halfSize;
        visibilityLimitAngle = (float) (Math.PI / 2 + Math.acos((halfSize + center.x) / hypotenuse));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (itemViews.isEmpty())
            return;
        laidOut = true;
        int centralMeasuredWidth = centralView.getMeasuredWidth();
        if (changed) {
            center.y = (b - t) / 2;
            int centralHalfHeight = centralView.getMeasuredHeight() / 2;
            centralView.layout(0, center.y - centralHalfHeight, centralMeasuredWidth, center.y + centralHalfHeight);
        }
        for (int i = 0; i < itemViews.size(); i++) {
            View child = itemViews.get(i);
            float angle = scrollAngleOffset + i * ANGLE_STEP;
            if (angle >= -visibilityLimitAngle && angle <= visibilityLimitAngle) {
                int halfSize = child.getMeasuredWidth() / 2;
                int hypotenuse = centralMeasuredWidth / 2 + innerPadding + halfSize;
                int centerX = (int) (center.x + hypotenuse * Math.cos(angle));
                int centerY = (int) (center.y + hypotenuse * Math.sin(angle));
                if (child.getVisibility() == View.GONE) {
                    child.setVisibility(View.VISIBLE);
                }
                child.layout(centerX - halfSize, centerY - halfSize, centerX + halfSize, centerY + halfSize);
            } else {
                child.setVisibility(View.GONE);
            }
        }
        invalidate();
    }

    // ***********************************
    // TOUCH HANDLING
    // ***********************************

    private static final float SCROLLING_MULTIPLIER = 0.002f;
    private static final float OVERSCROLL_MULTIPLIER = 2;
    private static final float DECELERATION = 0.00003f;
    private static final float FLING_VELOCITY_HISTORY_SIZE = 5;
    private static final float FLING_VELOCITY_MULTIPLIER = 9.0f;
    private static final float AVG_OVERSCROLL_BACK_VELOCITY = 0.0005f;
    private static final float MIN_VELOCITY = 0.004f;

    private float prevX;
    private float prevY;
    private boolean scrolling;
    private LinkedList<Float> velocityHistory = new LinkedList<Float>();
    private long prevTime;
    private Interpolator decelerateInterpolator = new DecelerateInterpolator();
    private Interpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    private ValueAnimator animator;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            velocityHistory.clear();
            if (animator != null) {
                animator.cancel();
            }
            prevX = ev.getX();
            prevY = ev.getY();
        } else if (!scrolling && ev.getAction() == MotionEvent.ACTION_MOVE && calculateDistance(ev.getX(), ev.getY(), prevX, prevY) > touchSlop) {
            scrolling = true;
            prevTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (itemViews.isEmpty())
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float distanceFromCenter = calculateDistance(center.x, center.y, x, y);
                float cos = (x - center.x) / distanceFromCenter;
                // y is reverted
                float sin = (center.y - y) / distanceFromCenter;
                float delta = (sin * (x - prevX) + cos * (y - prevY)) * SCROLLING_MULTIPLIER;
                float overscrollFactor = calculateOverscrollFactor(scrollAngleOffset + delta);
                scrollAngleOffset += delta * overscrollFactor;
                long time = System.currentTimeMillis();
                velocityHistory.add(delta / (time - prevTime));
                if (velocityHistory.size() > FLING_VELOCITY_HISTORY_SIZE) {
                    velocityHistory.removeFirst();
                }
                requestLayout();
                prevX = x;
                prevY = y;
                prevTime = time;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                scrolling = false;
                float sum = 0;
                for (float f : velocityHistory) {
                    sum += f;
                }
                float flingVelocity = velocityHistory.size() == 0 ? 0 : sum / velocityHistory.size() * FLING_VELOCITY_MULTIPLIER;
                animateFling(flingVelocity);
                break;
        }
        return true;
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private float calculateOverscrollFactor(float newOffset) {
        if ((itemViews.size() - 1) * ANGLE_STEP < Math.PI / 2) {
            float limit = -(itemViews.size() - 1) * ANGLE_STEP / 2;
            return Math.max(0, 1 - OVERSCROLL_MULTIPLIER * Math.abs(limit - newOffset));
        } else {
            float limit = (float) Math.PI / 4;
            if (newOffset > -limit) {
                return Math.max(0, 1 - OVERSCROLL_MULTIPLIER * (newOffset + limit));
            }
            float lastViewAngle = newOffset + (itemViews.size() - 1) * ANGLE_STEP;
            if (lastViewAngle < limit) {
                return Math.max(0, 1 - OVERSCROLL_MULTIPLIER * (limit - lastViewAngle));
            }
            return 1;
        }
    }

    private boolean animateOverscrollBack() {
        float to;
        if ((itemViews.size() - 1) * ANGLE_STEP < Math.PI / 2) {
            to = -(itemViews.size() - 1) * ANGLE_STEP / 2;
        } else {
            float limit = (float) Math.PI / 4;
            if (scrollAngleOffset > -limit) {
                to = -limit;
            } else if (scrollAngleOffset + (itemViews.size() - 1) * ANGLE_STEP < limit) {
                to = limit - (itemViews.size() - 1) * ANGLE_STEP;
            } else {
                return false;
            }
        }
        int duration = (int) (Math.abs(scrollAngleOffset - to) / AVG_OVERSCROLL_BACK_VELOCITY);
        animator = ValueAnimator.ofFloat(scrollAngleOffset, to)
                .setDuration(duration);
        animator.setInterpolator(accelerateDecelerateInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollAngleOffset = (Float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.start();
        return true;
    }

    private void animateFling(final float flingVelocity) {
        int duration = (int) (Math.abs(flingVelocity) / DECELERATION);
        animator = ValueAnimator.ofFloat(1, 0)
                .setDuration(duration);
        animator.setInterpolator(decelerateInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                float velocity = flingVelocity * value;
                velocity *= calculateOverscrollFactor(scrollAngleOffset + velocity);
                if (Math.abs(velocity) < MIN_VELOCITY) {
                    animator.cancel();
                    animateOverscrollBack();
                } else {
                    scrollAngleOffset += velocity;
                }
                requestLayout();
            }
        });
        animator.start();
    }

}
