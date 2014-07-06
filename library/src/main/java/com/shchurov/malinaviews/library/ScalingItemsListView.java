package com.shchurov.malinaviews.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class ScalingItemsListView extends ListView implements AbsListView.OnScrollListener {

    private static final float DEFAULT_SCALE_EXPONENT = 1.2f;
    private static final float DEFAULT_MIN_SCALE = 0.4f;

    private boolean magnifyToCenter = true;
    private float minScale = DEFAULT_MIN_SCALE;
    private float scaleExponent = DEFAULT_SCALE_EXPONENT;
    private OnScrollListener onScrollListener;

    public ScalingItemsListView(Context context) {
        super(context);
        init(null);
    }

    public ScalingItemsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScalingItemsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        recalculateChildScales();
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ScalingItemsListView);
            magnifyToCenter = a.getBoolean(R.styleable.ScalingItemsListView_magnifyToCenter, true);
            minScale = a.getFloat(R.styleable.ScalingItemsListView_minScale, DEFAULT_MIN_SCALE);
            scaleExponent = a.getFloat(R.styleable.ScalingItemsListView_scaleExponent, DEFAULT_SCALE_EXPONENT);
            a.recycle();
        }
        super.setOnScrollListener(this);
        setScrollingCacheEnabled(false);
    }

    private void recalculateChildScales() {
        if (getChildCount() == 0)
            return;
        float middle = getHeight() / 2;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMiddle = Math.min(getHeight(), Math.max(0, child.getY() + child.getHeight() / 2));
            float scale;
            if (magnifyToCenter) {
                scale = minScale + (float) Math.pow(1 - Math.abs((middle - childMiddle) / middle), scaleExponent) * (1 - minScale);
            } else {
                scale = minScale + (float) Math.pow(Math.abs((middle - childMiddle) / middle), scaleExponent) * (1 - minScale);
            }
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

}
