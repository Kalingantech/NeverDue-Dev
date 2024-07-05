package com.kalingantech.neverduedev.New_subscription;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.Home.Recycler_new_sub_Adapter;
import com.kalingantech.neverduedev.Home.Recycler_new_sub_model;
import com.kalingantech.neverduedev.R;

import java.util.ArrayList;


public class Subs_template_Fragment extends Fragment {

    ImageButton back_btn;

    FloatingActionButton floating_btn;

    RecyclerView recyclerView;
    Recycler_new_sub_Adapter R_adaper;
    private SearchView searchView;
    Subs_list single_sub;
    ArrayList<Recycler_new_sub_model> new_subs_lists = new ArrayList<Recycler_new_sub_model>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subs_list, container, false);


        floating_btn = view.findViewById(R.id.xml_add_new_sub);
        recyclerView = view.findViewById(R.id.xml_recyclerview);
        searchView = view.findViewById(R.id.xml_searchview);
        searchView.clearFocus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new_subs_lists.clear(); //this will ensure to clear this list on backup pressed, if not same list will be readded say 4 number wil become 8 itesm coming back from previous activity


        new_subs_lists.add(new Recycler_new_sub_model(R.drawable.baseline_settings_24, "Template 1", R.color.black));
        new_subs_lists.add(new Recycler_new_sub_model(R.drawable.baseline_settings_24, "Template 2", R.color.white));
        new_subs_lists.add(new Recycler_new_sub_model(R.drawable.baseline_settings_24, "Template 3", R.color.black));
        new_subs_lists.add(new Recycler_new_sub_model(R.drawable.baseline_settings_24, "Template 4", R.color.white));

        R_adaper = new Recycler_new_sub_Adapter(getActivity(), new_subs_lists);
        recyclerView.setAdapter(R_adaper);


        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_sub_Fragment fragment = new New_sub_Fragment();
                Bundle arguments = new Bundle();
                arguments.putInt("Image", R.drawable.baseline_settings_24);
                arguments.putString("Name", "NEW");
                arguments.putInt("Color", R.color.white);

                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, fragment, "null")
                        .commit(); //donot add it to backstack
            }
        });


        R_adaper.setonItemclicklisterner(new Recycler_new_sub_Adapter.single_item_click_interface() {
            @Override
            public void onitemclick(int image,String name,int colour) {

                New_sub_Fragment fragment = new New_sub_Fragment();
                Bundle arguments = new Bundle();
                arguments.putInt("Image", image);
                arguments.putString("Name", name);
                arguments.putInt("Color", colour);

                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, fragment, "null")
                        .addToBackStack("null")
                        .commit();

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterlist(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterlist(newText);
                return true;
            }
        });

        return view;
    }

    private void filterlist(String newText) {

        ArrayList<Recycler_new_sub_model> filter_subs_lists = new ArrayList<Recycler_new_sub_model>();


        for(Recycler_new_sub_model item : new_subs_lists){
            if(item.getSub_name().toLowerCase().contains(newText.toLowerCase())){
                filter_subs_lists.add(item);
            }
        }

        if (filter_subs_lists.isEmpty()) {

            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();

        } else {
            R_adaper.filterList(filter_subs_lists);
        }
    }


}
