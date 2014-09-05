package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shchurov.malinaviews.library.TagsView;
import com.shchurov.malinaviews.views.R;

public class FragmentTagsView extends DialogFragment {

    private static final int SPACING_DP = 5;

    public static FragmentTagsView createInstance() {
        return new FragmentTagsView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View layout = inflater.inflate(R.layout.fragment_tags_view, container, false);
        final TagsView tagsView = (TagsView) layout.findViewById(R.id.tags_view);
        tagsView.setTagsResourceId(R.layout.item_tag);
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SPACING_DP, getResources().getDisplayMetrics());
        tagsView.setSpacing(spacing, spacing);
        for (int i = 0; i < 4; i++) {
            tagsView.addTag("tag" + i);
        }
        tagsView.setOnTagClickListener(new TagsView.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                tagsView.removeTag(tag);
            }
        });
        layout.findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return layout;
    }

}
