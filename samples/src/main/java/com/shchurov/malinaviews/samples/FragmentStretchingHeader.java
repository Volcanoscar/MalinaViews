package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.shchurov.malinaviews.library.StretchingHeaderListView;
import com.shchurov.malinaviews.views.R;

public class FragmentStretchingHeader extends Fragment {

    private static final String[] items = {"Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8",
            "Item9", "Item10", "Item11", "Item12", "Item13", "Item14", "Item15", "Item16", "Item17", "Item18", "Item19", "Item20",
            "Item21", "Item22", "Item23", "Item24", "Item25", "Item26", "Item27", "Item28", "Item29", "Item30", "Item31", "Item32"};

    public static FragmentStretchingHeader createInstance() {
        return new FragmentStretchingHeader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView iv = new ImageView(getActivity());
        iv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        iv.setImageResource(R.drawable.large_sample);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        StretchingHeaderListView lv = (StretchingHeaderListView) inflater.inflate(R.layout.fragment_stretching_header, container, false);
        lv.addHeaderView(iv, null, false);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));
        return lv;
    }
}
