package com.kalingantech.neverduedev.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalingantech.neverduedev.R;

import java.util.ArrayList;

public class Pay_method_Adapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;


    public Pay_method_Adapter(Context context, ArrayList<String> list ) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.payment_single_row_data, null); // inflate the layout
        TextView textView = convertView.findViewById(R.id.xml_single_row_data); // get the reference of ImageView
        textView.setText(list.get(position));
        return convertView;
    }
}
