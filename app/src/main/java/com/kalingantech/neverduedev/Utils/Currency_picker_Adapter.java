package com.kalingantech.neverduedev.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalingantech.neverduedev.R;

import java.util.ArrayList;

//Recyclerview_Adapter


public class Currency_picker_Adapter extends RecyclerView.Adapter<Currency_picker_Adapter.Myviewholder> {

    Context context;
    ArrayList<Currency_logo_model> currencylist;
    single_item_click_interface listener;




    public Currency_picker_Adapter(Context context, ArrayList<Currency_logo_model> currencylist) {
        this.context = context;
        this.currencylist = currencylist;

    }

    public void filter_List(ArrayList<Currency_logo_model> currencylist) {
        this.currencylist = currencylist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_single, parent, false);
        return new Myviewholder(rview);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        holder.icon.setText(currencylist.get(position).getCurr_logo());
        holder.curr_code.setText(currencylist.get(position).getCurr_code());
        holder.curr_name.setText(currencylist.get(position).getCurr_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //listener goes null if we create new adapter during filter list, hence call "filter_List" during searchview filtering & dont remove "istener != null menthod
                //to ensure app crash
                if(listener != null){
                    int pos = holder.getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        listener.onitemclick(currencylist.get(holder.getAdapterPosition()).getCurr_logo(),currencylist.get(holder.getAdapterPosition()).getCurr_code(),
                                currencylist.get(holder.getAdapterPosition()).getCurr_name());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencylist.size();
    }


    public static class Myviewholder extends RecyclerView.ViewHolder {
        TextView icon, curr_code, curr_name;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.xml_search_currency_icon); // get the reference of ImageView
            curr_code = itemView.findViewById(R.id.xml_search_currency_code); // get the reference of ImageView
            curr_name = itemView.findViewById(R.id.xml_search_currency_name); // get the reference of ImageView


        }
    }

    public interface single_item_click_interface {
        void onitemclick(String symbol, String curr_code,String curr_name);
    }

    public void setonItemclicklisterner(single_item_click_interface listener) {
        this.listener = listener;
    }


}

