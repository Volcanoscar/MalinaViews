package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shchurov.malinaviews.library.ScalingViewPagerIndicator;
import com.shchurov.malinaviews.views.R;

public class FragmentScalingIndicator extends Fragment {

    private static final int[] COLORS = {android.R.color.holo_orange_dark, android.R.color.holo_orange_light, android.R.color.holo_blue_dark,
            android.R.color.holo_blue_light, android.R.color.holo_green_dark, android.R.color.holo_green_light,
            android.R.color.holo_purple, android.R.color.holo_red_dark, android.R.color.holo_red_light};

    private static final String ARGS_COLOR_ID = "color_id";

    public static FragmentScalingIndicator createInstance() {
        return new FragmentScalingIndicator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_scaling_indicator, container, false);
        ScalingViewPagerIndicator indicator = (ScalingViewPagerIndicator) layout.findViewById(R.id.indicator);
        ViewPager vp = (ViewPager) layout.findViewById(R.id.vp);
        fillViewPager(vp);
        indicator.setViewPager(vp);
        return layout;
    }

    private void fillViewPager(ViewPager vp) {
        Fragment[] fragments = new Fragment[COLORS.length];
        String[] titles = new String[COLORS.length];
        for (int i = 0; i < COLORS.length; i++) {
            fragments[i] = createPageFragment(COLORS[i]);
            titles[i] = "Item " + i;
        }
        vp.setAdapter(new ScalingIndicatorAdapter(fragments, titles));
    }

    private Fragment createPageFragment(int colorId) {
        FragmentSample fragment = new FragmentSample();
        Bundle args = new Bundle();
        args.putInt(ARGS_COLOR_ID, colorId);
        fragment.setArguments(args);
        return fragment;
    }

    public static class FragmentSample extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = new View(getActivity());
            view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(getResources().getColor(getArguments().getInt(ARGS_COLOR_ID)));
            return view;
        }

    }

    private class ScalingIndicatorAdapter extends FragmentPagerAdapter {

        Fragment[] fragments;
        String[] titles;

        private ScalingIndicatorAdapter(Fragment[] fragments, String[] titles) {
            super(getFragmentManager());
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

}
