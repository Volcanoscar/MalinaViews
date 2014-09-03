package com.shchurov.malinaviews.samples;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shchurov.malinaviews.library.CameraGridViewAdapter;
import com.shchurov.malinaviews.views.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentCameraGridView extends Fragment {

    private static final int[] IMAGES = {R.drawable.medium_sample_1, R.drawable.medium_sample_2, R.drawable.medium_sample_3};

    private CameraGridViewAdapter<Integer> adapter;

    public static FragmentCameraGridView createInstance() {
        return new FragmentCameraGridView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final GridView gv = (GridView) inflater.inflate(R.layout.fragment_camera_grid_view, container, false);
        List<Integer> items = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < 29; i++) {
            items.add(IMAGES[random.nextInt(IMAGES.length)]);
        }
        adapter = new CameraGridViewAdapter<Integer>(getActivity(), items) {
            @Override
            public View getNormalView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new SquareImageView(getActivity());
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                }
                ((ImageView) convertView).setImageResource(getItem(position));
                return convertView;
            }
        };
        gv.setAdapter(adapter);
        return gv;
    }

    private static class SquareImageView extends ImageView {

        private SquareImageView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

}
