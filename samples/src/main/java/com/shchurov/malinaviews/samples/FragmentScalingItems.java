package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shchurov.malinaviews.library.ScalingItemsListView;
import com.shchurov.malinaviews.samples.helpers.ScalingItemsAdapter;
import com.shchurov.malinaviews.views.R;

public class FragmentScalingItems extends Fragment {

    private static final int[] IMAGES = {R.drawable.medium_sample_1, R.drawable.medium_sample_2, R.drawable.medium_sample_3};

    public static FragmentScalingItems createInstance() {
        return new FragmentScalingItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_scaling_items, container, false);
        final ScalingItemsListView lv = (ScalingItemsListView) layout.findViewById(R.id.lv);
        SampleItem[] items = new SampleItem[100];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SampleItem("Item " + i, IMAGES[i % 3]);
        }
        lv.setAdapter(new ScalingItemsAdapter(getActivity(), items));
        return lv;
    }

    public static class SampleItem {

        public String text;
        public int image;

        public SampleItem(String text, int image) {
            this.text = text;
            this.image = image;
        }

    }

}
