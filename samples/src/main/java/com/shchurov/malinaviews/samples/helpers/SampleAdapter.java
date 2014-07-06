package com.shchurov.malinaviews.samples.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shchurov.malinaviews.views.R;

public class SampleAdapter extends ArrayAdapter<Item> {

    private int resId;

    public SampleAdapter(Context context, int resId, Item[] items) {
        super(context, resId, items);
        this.resId = resId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, parent, false);
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

        void applyData(Item item) {
            tv_text.setText(item.text);
            iv_image.setImageResource(item.image);
        }

    }

}