package com.kalingantech.neverduedev.New_subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kalingantech.neverduedev.R;

public class Icon_Adapter extends BaseAdapter {

    Context context;
    int logos[];
    LayoutInflater inflter;

    public Icon_Adapter(Context context, int[] logos ) {
        this.context = context;
        this.logos = logos;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return logos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.icon_card_vw, null); // inflate the layout
        ImageView icon = (ImageView) convertView.findViewById(R.id.xml_icon_image); // get the reference of ImageView
        icon.setImageResource(logos[position]); // set logo images
        return convertView;
    }
}
