package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shchurov.malinaviews.library.ScalingItemsListView;
import com.shchurov.malinaviews.samples.helpers.Item;
import com.shchurov.malinaviews.samples.helpers.SampleAdapter;
import com.shchurov.malinaviews.views.R;

public class FragmentScalingItems extends Fragment {

    private static final Item[] items = {new Item("Item 1", R.drawable.medium_sample_1), new Item("Item 2", R.drawable.medium_sample_2),
            new Item("Item 3", R.drawable.medium_sample_3), new Item("Item 4", R.drawable.medium_sample_1),
            new Item("Item 5", R.drawable.medium_sample_2), new Item("Item 6", R.drawable.medium_sample_3),
            new Item("Item 7", R.drawable.medium_sample_1), new Item("Item 8", R.drawable.medium_sample_2),
            new Item("Item 9", R.drawable.medium_sample_3), new Item("Item 10", R.drawable.medium_sample_1),
            new Item("Item 11", R.drawable.medium_sample_2), new Item("Item 12", R.drawable.medium_sample_3),
            new Item("Item 13", R.drawable.medium_sample_1), new Item("Item 14", R.drawable.medium_sample_2),
            new Item("Item 15", R.drawable.medium_sample_3), new Item("Item 16", R.drawable.medium_sample_1),
            new Item("Item 17", R.drawable.medium_sample_2), new Item("Item 18", R.drawable.medium_sample_3),
            new Item("Item 19", R.drawable.medium_sample_1), new Item("Item 20", R.drawable.medium_sample_2),
            new Item("Item 21", R.drawable.medium_sample_3), new Item("Item 22", R.drawable.medium_sample_1),
            new Item("Item 23", R.drawable.medium_sample_2), new Item("Item 24", R.drawable.medium_sample_3)};

    public static FragmentScalingItems createInstance() {
        return new FragmentScalingItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_scaling_items, container, false);
        final ScalingItemsListView lv = (ScalingItemsListView) layout.findViewById(R.id.lv);
        lv.setAdapter(new SampleAdapter(getActivity(), R.layout.item_scaling, items));
        return lv;
    }

}
