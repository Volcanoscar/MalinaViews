package com.shchurov.malinaviews.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shchurov.malinaviews.library.ScalingItemsListView;
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
        fillListView(lv);
        return lv;
    }

    private void fillListView(ListView lv) {
        SampleItem[] items = new SampleItem[100];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SampleItem("Item " + i, IMAGES[i % 3]);
        }
        lv.setAdapter(new ScalingItemsAdapter(items));
    }

    private class SampleItem {

        String text;
        int image;

        SampleItem(String text, int image) {
            this.text = text;
            this.image = image;
        }

    }

    private class ScalingItemsAdapter extends ArrayAdapter<SampleItem> {

        private static final int RES_ID = R.layout.item_scaling;

        ScalingItemsAdapter(SampleItem[] items) {
            super(getActivity(), RES_ID, items);
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

        class ViewHolder {

            TextView tvText;
            ImageView ivImage;

            ViewHolder(View convertView) {
                tvText = (TextView) convertView.findViewById(R.id.tv_text);
                ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            }

            void applyData(SampleItem item) {
                tvText.setText(item.text);
                ivImage.setImageResource(item.image);
            }

        }

    }

}
