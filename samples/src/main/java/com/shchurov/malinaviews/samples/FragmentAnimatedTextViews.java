package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shchurov.malinaviews.library.HoppingTextView;
import com.shchurov.malinaviews.views.R;

public class FragmentAnimatedTextViews extends Fragment implements View.OnClickListener {

    public static FragmentAnimatedTextViews createInstance() {
        return new FragmentAnimatedTextViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_animated_text_views, container, false);
        layout.findViewById(R.id.tv_hopping).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hopping:
                ((HoppingTextView) v).playHoppingAnimation();
                break;
        }
    }
}
