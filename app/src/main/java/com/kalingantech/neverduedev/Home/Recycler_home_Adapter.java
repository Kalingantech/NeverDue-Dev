package com.kalingantech.neverduedev.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.R;

import java.util.ArrayList;
import java.util.Calendar;

//Recyclerview_Adapter


public class Recycler_home_Adapter extends RecyclerView.Adapter<Recycler_home_Adapter.Myviewholder> {

    Context context;
    ArrayList<Subs_list> subsModels;

    single_item_click_interface listener;

    pay_btn_interface paid_listener;

    SharedPreferences sharedPreferences;

    public Recycler_home_Adapter(Context context) {
        this.context = context;

    }

    public void set_sub_list(ArrayList<Subs_list> subsModels) {
        this.subsModels = subsModels;

    }

    public void filterList(ArrayList<Subs_list> filterlist) {
        this.subsModels = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub_model_card, parent, false);
        return new Myviewholder(rview);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {


        //to convert string into uri - "Uri.parse"
        Glide.with(context)
                .load(Uri.parse(subsModels.get(position).getSub_image()))
                .into(holder.imageView);

        sharedPreferences = context.getSharedPreferences("PREF", 0);
        String symbol = sharedPreferences.getString("CURRENCY_LOGO", "");
        String amount = symbol +" "+ String.valueOf(subsModels.get(position).getPrice());

        holder.name_tv.setText(subsModels.get(position).getName());
        holder.amount_tv.setText(amount);

        //getting date from DB

        String getdate = getfuturedate(subsModels.get(position).getBilling_next_date().getTime());


        //String date = new SimpleDateFormat("dd-MMM", Locale.getDefault()).format(subsModels.get(position).getBilling_next_date().getTime());
        holder.date_tv.setText(getdate);

        //---------------pay button----------
        if(getdate.equals("Expired") || getdate.equals("Today")){
            holder.pay_btn.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.GRAY);
        }

        holder.pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paid_listener.onitemclick(subsModels.get(position));
                holder.pay_btn.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        });
        //---------------pay button----------

        //getting date
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int uid = subsModels.get(holder.getAdapterPosition()).getUid();
                listener.onitemclick(uid);

                //listener.onitemclick(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return subsModels.size();
    }


    public static class Myviewholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageButton pay_btn;
        TextView name_tv, amount_tv, date_tv;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.xml_home_sub_image);
            name_tv = itemView.findViewById(R.id.xml_home_sub_name);
            amount_tv = itemView.findViewById(R.id.xml_home_sub_amount);
            date_tv = itemView.findViewById(R.id.xml_home_sub_date);
            pay_btn = itemView.findViewById(R.id.xml_pay_btn);
        }
    }

    public interface single_item_click_interface {
        void onitemclick(int uid);
    }

    public void setonItemclicklisterner(single_item_click_interface listener) {
        this.listener = listener;
    }

    public interface pay_btn_interface {
        void onitemclick(Subs_list model);
    }

    public void setonItemclicklisterner(pay_btn_interface paid_listener) {
        this.paid_listener = paid_listener;
    }


    //------------------------------ adapter completed----------------------------------------------------------
    private String getfuturedate(long date) {

        String return_data = "NULL";
        int days;
        Calendar current_cal = Calendar.getInstance();
        Calendar reference_cal = Calendar.getInstance();
        reference_cal.setTimeInMillis(date);

        int year_diff = reference_cal.get(Calendar.YEAR) - current_cal.get(Calendar.YEAR);

        if (reference_cal.before(current_cal)) {
            return_data = "Expired";

        } else if (reference_cal.equals(current_cal)) {
            return_data = "Today";
        } else if(year_diff<1){

            days = reference_cal.get(Calendar.DAY_OF_YEAR) - current_cal.get(Calendar.DAY_OF_YEAR);
            if(days==1){
                return_data = String.valueOf(days) + " Day";
            } else if (days<=7) {
                return_data = String.valueOf(days) + " Days";
            } else if (days>7 && days<=31) {
                int week_data = days/7;
                if(week_data == 1){
                    return_data = String.valueOf(week_data) + " Week";
                } else {
                    return_data = String.valueOf(week_data) + " Weeks";
                }
            } else if (days>32 && days<365) {
                int month_data = days/30;
                if(month_data == 1){
                    return_data = String.valueOf(month_data) + " Month";
                } else {
                    return_data = String.valueOf(month_data) + " Months";
                }
            }else {
                int year_data = days/365;
                if(year_data <= 1){
                    return_data = String.valueOf(year_data) + " Year";
                } else {
                    return_data = String.valueOf(year_data) + " Years";
                }
            }

        }else if(year_diff>=1){
            int years = reference_cal.get(Calendar.YEAR) - current_cal.get(Calendar.YEAR);

            if(years<=1){
                return_data = String.valueOf(years+"+") + " Year";
            }else{
                return_data = String.valueOf(years) + "+ Years";
            }
        }
        return return_data;

    }

}

