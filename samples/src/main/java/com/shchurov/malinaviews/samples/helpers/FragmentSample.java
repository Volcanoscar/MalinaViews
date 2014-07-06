package com.shchurov.malinaviews.samples.helpers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FragmentSample extends Fragment {

    private static final String ARGS_COLOR_ID = "color_id";

    public static FragmentSample createInstance(int colorId) {
        FragmentSample fragment = new FragmentSample();
        Bundle args = new Bundle();
        args.putInt(ARGS_COLOR_ID, colorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = new View(getActivity());
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        view.setBackgroundColor(getResources().getColor(getArguments().getInt(ARGS_COLOR_ID)));
        return view;
    }



}
