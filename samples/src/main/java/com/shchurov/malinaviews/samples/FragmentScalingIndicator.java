package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shchurov.malinaviews.library.ScalingViewPagerIndicator;
import com.shchurov.malinaviews.samples.helpers.FragmentSample;
import com.shchurov.malinaviews.views.R;

public class FragmentScalingIndicator extends Fragment {

    private static final int[] COLORS = {android.R.color.holo_orange_dark, android.R.color.holo_orange_light, android.R.color.holo_blue_dark,
            android.R.color.holo_blue_light, android.R.color.holo_green_dark, android.R.color.holo_green_light,
            android.R.color.holo_purple, android.R.color.holo_red_dark, android.R.color.holo_red_light};

    public static FragmentScalingIndicator createInstance() {
        return new FragmentScalingIndicator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_scaling_indicator, container, false);
        final ScalingViewPagerIndicator indicator = (ScalingViewPagerIndicator) layout.findViewById(R.id.indicator);
        ViewPager vp = (ViewPager) layout.findViewById(R.id.vp);
        final Fragment[] fragments = new Fragment[COLORS.length];
        final String[] titles = new String[COLORS.length];
        for (int i = 0 ; i < COLORS.length; i++) {
            fragments[i] = FragmentSample.createInstance(COLORS[i]);
            titles[i] = "Item " + i;
        }
        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        });
        indicator.setViewPager(vp);
        return layout;
    }
}
