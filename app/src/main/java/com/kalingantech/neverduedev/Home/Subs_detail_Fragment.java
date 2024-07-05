package com.kalingantech.neverduedev.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.New_subscription.Edit_sub_Fragment;
import com.kalingantech.neverduedev.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Subs_detail_Fragment extends Fragment {


    Button edit_btn,delete_btn;
    private Subs_ViewModel subs_viewModel;

    TextView total_paid,billing_date;
    Subs_list delete_subs_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subs_detail, container, false);

        //============================
        Bundle arguments = getArguments();
        int UID = arguments.getInt("UID");
        //============================

        billing_date =view.findViewById(R.id.xml_billing_date);
        total_paid = view.findViewById(R.id.xml_total_paid);

        edit_btn = view.findViewById(R.id.xml_sub_detail_edit_btn);
        delete_btn = view.findViewById(R.id.xml_sub_detail_delete_btn);


        subs_viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(Subs_ViewModel.class);
        subs_viewModel.getSearch_list_by_uid(UID).observe(getViewLifecycleOwner(),subs_list -> {
            delete_subs_list = subs_list;

            String due_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(subs_list.getBilling_next_date().getTime());
            billing_date.setText(due_date);

            total_paid.setText(String.valueOf(subs_list.getTotal_paid()));


        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_sub_Fragment fragment = new Edit_sub_Fragment();
                Bundle arguments = new Bundle();
                arguments.putInt("UID", UID);
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, fragment, "null").addToBackStack("null")
                        .commit();
            }
        });


        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subs_viewModel.delete_sub_rep(delete_subs_list);
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Home_Fragment()).commit();
            }
        });

        return view;
    }
}