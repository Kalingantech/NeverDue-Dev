package com.kalingantech.neverduedev.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalingantech.neverduedev.DB.Profile_list;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.Home.Recycler_home_Adapter;
import com.kalingantech.neverduedev.R;

import java.util.ArrayList;
import java.util.List;

public class Profile_list_Adapter extends RecyclerView.Adapter<Profile_list_Adapter.Myviewholder> {

    Context context;
    ArrayList<Profile_list> profsModels;

    single_item_click_interface listener;

    public Profile_list_Adapter(Context context,ArrayList<Profile_list> profsModels) {
        this.context = context;
        this.profsModels = profsModels;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_single_row_data, parent, false);
        return new Profile_list_Adapter.Myviewholder(rview);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        holder.name_tv.setText(profsModels.get(position).getProfile_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = profsModels.get(holder.getAdapterPosition()).getProfile_name();
                listener.onitemclick(name);

                //listener.onitemclick(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return profsModels.size();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder {

        TextView name_tv;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.xml_profile_single_row_data);

        }
    }

    public interface single_item_click_interface {
        void onitemclick(String prof_name);
    }

    public void setonItemclicklisterner(single_item_click_interface listener) {
        this.listener = listener;
    }

}
