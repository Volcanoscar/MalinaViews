package com.shchurov.malinaviews.samples;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shchurov.malinaviews.library.CircularScrollView;
import com.shchurov.malinaviews.views.R;

public class FragmentCircularScrollView extends Fragment {

    private static final int ITEMS_SIZE_DP = 100;

    public static FragmentCircularScrollView createInstance() {
        return new FragmentCircularScrollView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_circular_scrollview, container, false);
        CircularScrollView circular_scrollview = (CircularScrollView) layout.findViewById(R.id.circular_scrollview);
        ImageView centralView = new ImageView(getActivity());
        centralView.setImageResource(R.drawable.circle_sample);
        circular_scrollview.setCentralView(centralView);
        int itemsSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ITEMS_SIZE_DP, getResources().getDisplayMetrics());
        for (int i = 0; i < 11; i++) {
            CircleTextView view = new CircleTextView(getActivity(), "item " + i);
            view.setLayoutParams(new ViewGroup.LayoutParams(itemsSize, itemsSize));
            circular_scrollview.addView(view);
        }
        return layout;
    }

    private static class CircleTextView extends TextView {

        static final int PADDING_DP = 5;
        static final int STROKE_WIDTH_DP = 1;

        Paint paint;
        int strokeWidth;

        private CircleTextView(Context context, String text) {
            super(context);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH_DP, getResources().getDisplayMetrics());
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            setText(text);
            setTextSize(20);
            setGravity(Gravity.CENTER);
            setTextColor(Color.BLACK);
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_DP, getResources().getDisplayMetrics());
            setPadding(padding, 0, padding, 0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float radius = getMeasuredWidth() / 2;
            canvas.drawCircle(radius, radius, radius - strokeWidth, paint);
        }

    }
}
