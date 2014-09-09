package com.shchurov.malinaviews.samples;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.shchurov.malinaviews.views.R;


public class ActivityMain extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.raspberry)));
        findViewById(R.id.btn_stretching_header).setOnClickListener(this);
        findViewById(R.id.btn_scaling_items).setOnClickListener(this);
        findViewById(R.id.btn_scaling_indicator).setOnClickListener(this);
        findViewById(R.id.btn_camera_gridview).setOnClickListener(this);
        findViewById(R.id.btn_tagsview).setOnClickListener(this);
        findViewById(R.id.btn_animated_textviews).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stretching_header:
                openFragment(FragmentStretchingHeader.createInstance());
                break;
            case R.id.btn_scaling_items:
                openFragment(FragmentScalingItems.createInstance());
                break;
            case R.id.btn_scaling_indicator:
                openFragment(FragmentScalingIndicator.createInstance());
                break;
            case R.id.btn_camera_gridview:
                openFragment(FragmentCameraGridView.createInstance());
                break;
            case R.id.btn_tagsview:
                FragmentTagsView.createInstance().show(getSupportFragmentManager(), FragmentTagsView.class.getName());
                break;
            case R.id.btn_animated_textviews:
                openFragment(FragmentAnimatedTextViews.createInstance());
                break;
        }
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
