package com.kalingantech.neverduedev.New_subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalingantech.neverduedev.R;

import java.util.ArrayList;

public class Reminder_picker_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Reminder_model> reminderlist;


    public Reminder_picker_Adapter(Context context, ArrayList<Reminder_model> currencylist ) {
        this.context = context;
        this.reminderlist = currencylist;
    }

    @Override
    public int getCount() {
        return reminderlist.size();
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
        textView.setText(reminderlist.get(position).string_days);
        return convertView;
    }
}
