package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NasaItemAdapter extends ArrayAdapter<NasaItem> {
    private Context context;
    private List<NasaItem> nasaItemList;

    public NasaItemAdapter(Context context, List<NasaItem> nasaItemList) {
        super(context, 0, nasaItemList);
        this.context = context;
        this.nasaItemList = nasaItemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NasaItem nasaItem = nasaItemList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvCopyright = convertView.findViewById(R.id.tvCopyright);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        tvTitle.setText(nasaItem.getTitle());
        tvDate.setText(nasaItem.getDate());
        tvCopyright.setText(nasaItem.getCopyright());
        Glide.with(context).load(nasaItem.getImgUrl()).into(imageView);

        return convertView;
    }
}
