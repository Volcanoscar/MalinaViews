package com.shchurov.malinaviews.library;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScalingViewPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private static final int DEFAULT_TITLES_ON_SCREEN = 3;
    private static final float DEFAULT_MIN_SCALE = 0.3f;
    private static final float DEFAULT_SCALE_EXPONENT = 2f;
    private static final float DEFAULT_MIN_ALPHA = 0.5f;
    private static final int DEFAULT_TEXT_SIZE = 40;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;

    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private LinearLayout titlesLayout;
    private int titlesOnScreen = DEFAULT_TITLES_ON_SCREEN;
    private float minScale = DEFAULT_MIN_SCALE;
    private float scaleExponent = DEFAULT_SCALE_EXPONENT;
    private float minAlpha = DEFAULT_MIN_ALPHA;
    private float textSize = DEFAULT_TEXT_SIZE;
    private int textColor = DEFAULT_TEXT_COLOR;

    public ScalingViewPagerIndicator(Context context) {
        super(context);
        init();
    }

    public ScalingViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScalingViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        titlesLayout = new LinearLayout(getContext());
        setHorizontalScrollBarEnabled(false);
        titlesLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        titlesLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(titlesLayout);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        if (viewPager.getAdapter() == null)
            throw new IllegalStateException("ViewPager's adapter is null");
        viewPager.setOnPageChangeListener(this);
        notifyDataSetChanged();
        post(new Runnable() {
            @Override public void run() {
                calculateTitlesWidth();
            }
        });
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        onPageChangeListener = listener;
    }

    public void notifyDataSetChanged() {
        refreshTitles();
        removeRedundantTitles();
    }

    private void refreshTitles() {
        PagerAdapter adapter = viewPager.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            TextView tv;
            if (titlesLayout.getChildCount() == i) {
                tv = createTitleView();
                titlesLayout.addView(tv);
            } else {
                tv = (TextView) getChildAt(i);
            }
            tv.setText(adapter.getPageTitle(i));
        }
    }

    private TextView createTitleView() {
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(getWidth() / titlesOnScreen, LayoutParams.WRAP_CONTENT));
        tv.setTextColor(textColor);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextSize(textSize);
        return tv;
    }

    private void removeRedundantTitles() {
        PagerAdapter adapter = viewPager.getAdapter();
        if (titlesLayout.getChildCount() > adapter.getCount()) {
            titlesLayout.removeViews(adapter.getCount(), titlesLayout.getChildCount() - adapter.getCount());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View titleView = titlesLayout.getChildAt(position);
        int nextTitleViewWidth = position == titlesLayout.getChildCount() - 1 ? 0 : titlesLayout.getChildAt(position + 1).getWidth();
        float length = (titleView.getWidth() + nextTitleViewWidth) / 2;
        int offset = (int) (titleView.getX() - (getWidth() - titleView.getWidth()) / 2 + length * positionOffset);
        scrollTo(offset, 0);
        recalculateTitlesScalesAndAlpha();
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void calculateTitlesWidth() {
        for (int i = 0; i < titlesLayout.getChildCount(); i++) {
            View titleView = titlesLayout.getChildAt(i);
            titleView.getLayoutParams().width = getWidth() / titlesOnScreen;
            titleView.forceLayout();
        }
        int padding = (getWidth() - titlesLayout.getChildAt(0).getLayoutParams().width) / 2;
        titlesLayout.setPadding(padding, 0, padding, 0);
        titlesLayout.setClipToPadding(false);
        titlesLayout.requestLayout();
        titlesLayout.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                titlesLayout.removeOnLayoutChangeListener(this);
                recalculateTitlesScalesAndAlpha();
            }
        });
    }

    private void recalculateTitlesScalesAndAlpha() {
        float middle = getWidth() / 2;
        for (int i = 0; i < titlesLayout.getChildCount(); i++) {
            View child = titlesLayout.getChildAt(i);
            if (child.getX() > getWidth() + getScrollX() || child.getX() + child.getWidth() < getScrollX())
                continue;
            float childMiddle = child.getX() + child.getWidth() / 2 - getScrollX();
            float scale = minScale + (float) Math.pow(1 - Math.abs((middle - childMiddle) / middle), scaleExponent) * (1 - minScale);
            child.setScaleX(scale);
            child.setScaleY(scale);
            float alpha = minAlpha + (float) Math.pow(1 - Math.abs((middle - childMiddle) / middle), scaleExponent) * (1 - minAlpha);
            child.setAlpha(alpha);
        }
    }

}
