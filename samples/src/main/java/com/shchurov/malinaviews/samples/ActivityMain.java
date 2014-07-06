package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.shchurov.malinaviews.views.R;


public class ActivityMain extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_stretching_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, FragmentStretchingHeader.createInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        findViewById(R.id.btn_scaling_items).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, FragmentScalingItems.createInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        findViewById(R.id.btn_scaling_indicator).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, FragmentScalingIndicator.createInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
