package com.kalingantech.neverduedev.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kalingantech.neverduedev.R;

import java.util.ArrayList;

//Recyclerview_Adapter


public class Recycler_new_sub_Adapter extends RecyclerView.Adapter<Recycler_new_sub_Adapter.Myviewholder> {

    Context context;
    ArrayList<Recycler_new_sub_model> newsubsModels;

    single_item_click_interface listener;

    public Recycler_new_sub_Adapter(Context context, ArrayList<Recycler_new_sub_model> subsModels) {
        this.context = context;
        this.newsubsModels = subsModels;
    }


    public void filterList( ArrayList<Recycler_new_sub_model> filterlist) {
        this.newsubsModels = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_sub_model_card, parent, false);
        return new Myviewholder(rview);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        //to convert string into uri - "Uri.parse"
        Glide.with(context)
                        .load(newsubsModels.get(position).getSub_image())
                                .into(holder.imageView);

        holder.textView.setText(newsubsModels.get(position).getSub_name());
        holder.itemView.setBackgroundColor(newsubsModels.get(position).getColour());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //int uid = subsModels.get(holder.getAdapterPosition()).getUid();
                listener.onitemclick(newsubsModels.get(holder.getAdapterPosition()).getSub_image(),newsubsModels.get(holder.getAdapterPosition()).getSub_name(),newsubsModels.get(holder.getAdapterPosition()).getColour());
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsubsModels.size();
    }


    public class Myviewholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.xml_sub_image);
            textView = itemView.findViewById(R.id.xml_sub_name);

        }
    }

    public interface single_item_click_interface {
        void onitemclick(int image,String name,int color);
    }

    public void setonItemclicklisterner(single_item_click_interface listener) {
        this.listener = listener;
    }
}

