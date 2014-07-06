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

    private FragmentSample[] items = {FragmentSample.createInstance(android.R.color.holo_blue_dark),
            FragmentSample.createInstance(android.R.color.holo_blue_light), FragmentSample.createInstance(android.R.color.holo_green_dark),
            FragmentSample.createInstance(android.R.color.holo_green_light), FragmentSample.createInstance(android.R.color.holo_orange_dark),
            FragmentSample.createInstance(android.R.color.holo_orange_light), FragmentSample.createInstance(android.R.color.holo_purple),
            FragmentSample.createInstance(android.R.color.holo_red_dark), FragmentSample.createInstance(android.R.color.holo_red_light)};

    private String[] titles = {"Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"};

    public static FragmentScalingIndicator createInstance() {
        return new FragmentScalingIndicator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_scaling_indicator, container, false);
        final ScalingViewPagerIndicator indicator = (ScalingViewPagerIndicator) layout.findViewById(R.id.indicator);
        ViewPager vp = (ViewPager) layout.findViewById(R.id.vp);
        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return items[position];
            }

            @Override
            public int getCount() {
                return items.length;
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
