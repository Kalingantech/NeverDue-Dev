package com.kalingantech.neverduedev.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalingantech.neverduedev.R;

import java.util.ArrayList;

public class Currency_picker_BaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<Currency_logo_model> currencylist;


    public Currency_picker_BaseAdapter(Context context, ArrayList<Currency_logo_model> currencylist ) {
        this.context = context;
        this.currencylist = currencylist;
    }

    @Override
    public int getCount() {
        return currencylist.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.currency_single, null); // inflate the layout
        TextView icon = convertView.findViewById(R.id.xml_search_currency_icon); // get the reference of ImageView
        TextView curr_code = convertView.findViewById(R.id.xml_search_currency_code); // get the reference of ImageView
        TextView curr_name = convertView.findViewById(R.id.xml_search_currency_name); // get the reference of ImageView
        icon.setText(currencylist.get(position).getCurr_logo());
        curr_code.setText(currencylist.get(position).getCurr_code());
        curr_name.setText(currencylist.get(position).getCurr_name());
        return convertView;
    }
}
