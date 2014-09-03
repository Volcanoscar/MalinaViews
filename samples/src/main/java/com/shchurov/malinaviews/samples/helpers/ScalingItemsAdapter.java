package com.shchurov.malinaviews.samples.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shchurov.malinaviews.views.R;

import static com.shchurov.malinaviews.samples.FragmentScalingItems.*;

public class ScalingItemsAdapter extends ArrayAdapter<SampleItem> {

    private static final int RES_ID = R.layout.item_scaling;

    public ScalingItemsAdapter(Context context, SampleItem[] items) {
        super(context, RES_ID, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(RES_ID, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.applyData(getItem(position));
        return convertView;
    }

    private class ViewHolder {

        TextView tv_text;
        ImageView iv_image;

        ViewHolder(View convertView) {
            tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
        }

        void applyData(SampleItem item) {
            tv_text.setText(item.text);
            iv_image.setImageResource(item.image);
        }

    }

}