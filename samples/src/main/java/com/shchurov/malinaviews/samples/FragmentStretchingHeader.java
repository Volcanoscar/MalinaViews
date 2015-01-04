package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.shchurov.malinaviews.library.StretchingHeaderListView;
import com.shchurov.malinaviews.views.R;

public class FragmentStretchingHeader extends Fragment {

    public static FragmentStretchingHeader createInstance() {
        return new FragmentStretchingHeader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StretchingHeaderListView lv = (StretchingHeaderListView) inflater.inflate(R.layout.fragment_stretching_header, container, false);
        lv.addHeaderView(createHeaderView(), null, false);
        fillListView(lv);
        return lv;
    }

    private View createHeaderView() {
        ImageView iv = new ImageView(getActivity());
        iv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        iv.setImageResource(R.drawable.large_sample);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }

    private void fillListView(ListView lv) {
        String[] items = new String[20];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Item " + i;
        }
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));
    }
}
