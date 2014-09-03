package com.shchurov.malinaviews.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;

public class StretchingHeaderListView extends ListView {

    private static final float DEFAULT_FLEXIBILITY = 0.6f;
    private static final int DEFAULT_BACK_ANIMATION_TIME = 600;

    private View headerView;
    private int staticHeight;
    private boolean stretching;
    private float prevY;
    private float flexibility = DEFAULT_FLEXIBILITY;
    private int backAnimationTime = DEFAULT_BACK_ANIMATION_TIME;

    public StretchingHeaderListView(Context context) {
        super(context);
        init();
    }

    public StretchingHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StretchingHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        if (getHeaderViewsCount() != 0) {
            throw new IllegalStateException("Only one header is allowed");
        }
        super.addHeaderView(v, data, isSelectable);
        headerView = v;
        staticHeight = headerView.getHeight();
        if (staticHeight == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    staticHeight = headerView.getHeight();
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (headerView == null)
            return super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN && isScrollOnTop()) {
            prevY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && isScrollOnTop()) {
            float delta = (event.getY() - prevY) * (staticHeight * flexibility / headerView.getHeight());
            prevY = event.getY();
            if (delta > 0) {
                stretching = true;
            }
            if (stretching) {
                int newHeight = (int) Math.max(staticHeight, headerView.getHeight() + delta);
                headerView.getLayoutParams().height = newHeight;
                headerView.requestLayout();
                return true;
            }
        } else if (stretching && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
            stretching = false;
            if (headerView.getHeight() != staticHeight) {
                animateToStaticState();
            }
        }
        return super.onTouchEvent(event);
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private boolean isScrollOnTop() {
        return getChildCount() == 0 || getChildAt(0).getTop() == 0;
    }

    private void animateToStaticState() {
        ValueAnimator animator = ValueAnimator.ofInt(headerView.getHeight(), staticHeight);
        animator.setDuration(backAnimationTime);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                headerView.getLayoutParams().height = value;
                headerView.requestLayout();
            }
        });
        animator.start();
    }

}
