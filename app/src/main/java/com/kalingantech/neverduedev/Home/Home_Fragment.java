package com.kalingantech.neverduedev.Home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.New_subscription.Subs_template_Fragment;
import com.kalingantech.neverduedev.R;
import com.kalingantech.neverduedev.Setting_Fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home_Fragment extends Fragment {

    FloatingActionButton floating_btn;
    ImageButton sorting_btn, setting_btn;
    RecyclerView recyclerView;
    Recycler_home_Adapter R_adaper;
    List<Subs_list> subs_lists = new ArrayList<Subs_list>();

    Subs_list subs_lists_pay;
    private Subs_ViewModel subs_viewModel;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor pref_editor;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int selected_radio;
    String selected_sorting_type, curr_symbol;

    TextView add_subscription,average_usage;

    ConstraintLayout average_usage_lt;
    double average_usage_month_year;
    String btm_avg_month_year_swtich;

    TextView average_usage_tv;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        floating_btn = view.findViewById(R.id.xml_btm_sheet);
        sorting_btn = view.findViewById(R.id.xml_sorting);
        setting_btn = view.findViewById(R.id.xml_setting_btn);



        recyclerView = view.findViewById(R.id.xml_recyclerView_home);
        add_subscription = view.findViewById(R.id.xml_add_subscription);
        add_subscription.setVisibility(View.GONE);

        average_usage_lt =view.findViewById(R.id.xml_average_usage_lt);
        average_usage = view.findViewById(R.id.xml_average_usage);
        average_usage_tv = view.findViewById(R.id.xml_average_usage_tv);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        R_adaper = new Recycler_home_Adapter(getActivity());
        subs_viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(Subs_ViewModel.class);


        sharedPreferences = getActivity().getSharedPreferences("PREF", 0);
        pref_editor = sharedPreferences.edit();

        //---------------------------
        average_Monthly_usage_cal();
        //---------------------------

        average_usage_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                average_Month_year_cal();
            }
        });


        selected_sorting_type = sharedPreferences.getString("SORT", "Price"); //"Price" is default value means used only for first before changing.
        curr_symbol = sharedPreferences.getString("CURRENCY_LOGO", "");

        if (selected_sorting_type.equals("Price")) {
            loaddata_byprice();
        } else if (selected_sorting_type.equals("Name")) {
            loaddata_byname();
        } else if (selected_sorting_type.equals("Date")) {
            loaddata_bydate();
        }


        R_adaper.setonItemclicklisterner(new Recycler_home_Adapter.single_item_click_interface() {
            @Override
            public void onitemclick(int uid) {
                Subs_detail_Fragment fragment = new Subs_detail_Fragment();
                Bundle arguments = new Bundle();
                arguments.putInt("UID", uid);
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, fragment, "null").addToBackStack("null")
                        .commit();
            }
        });

        R_adaper.setonItemclicklisterner(new Recycler_home_Adapter.pay_btn_interface() {
            @Override
            public void onitemclick(Subs_list model) {

                Subs_list updated_item = model;
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(model.getBilling_next_date().getTime());

                Date existing_date = cal.getTime(); //exisint payable date.
                int steps= updated_item.getBillingcycle_no(); //1,2...
                String type = updated_item.getBillingcycle_step_type();// daily,weekly
                if(type.equals("Daily")){
                    cal.add(Calendar.DAY_OF_YEAR,steps);
                    Date new_date = cal.getTime();
                    model.setBilling_next_date(new_date);
                    String date = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(new_date);
                    Toast.makeText(getContext(),"Paid for " + String.valueOf(date),Toast.LENGTH_SHORT).show();
                } else if (type.equals("Weekly")) {
                    cal.add(Calendar.WEEK_OF_YEAR,steps);
                    Date new_date = cal.getTime();
                    model.setBilling_next_date(new_date);
                    String date = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(new_date);
                    Toast.makeText(getContext(),"Paid for " + String.valueOf(date),Toast.LENGTH_SHORT).show();
                } else if (type.equals("Monthly")) {
                    cal.add(Calendar.MONTH,steps);
                    Date new_date = cal.getTime();
                    model.setBilling_next_date(new_date);
                    String date = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(new_date);
                    Toast.makeText(getContext(),"Paid for " + String.valueOf(date),Toast.LENGTH_SHORT).show();
                } else if (type.equals("Yearly")) {
                    cal.add(Calendar.YEAR,steps);
                    Date new_date = cal.getTime();
                    model.setBilling_next_date(new_date);
                    String date = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(new_date);
                    Toast.makeText(getContext(),"Paid for " + String.valueOf(date),Toast.LENGTH_SHORT).show();
                }

                updated_item.setTotal_paid(model.getTotal_paid() + model.getPrice());
                subs_viewModel.update_sub_rep(updated_item);

            }
        });


        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----------------------------------------
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Subs_template_Fragment()).addToBackStack("null").commit();
                //-----------------------------------------
            }
        });

        sorting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog sort_dialog = new Dialog(getContext());
                sort_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sort_dialog.setContentView(R.layout.fragment_home_sorting);
                sort_dialog.show();
                sort_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                radioGroup = sort_dialog.findViewById(R.id.groupradio);

                if (selected_sorting_type.equals("Price")) {
                    radioGroup.check(R.id.xml_sort_by_price);
                } else if (selected_sorting_type.equals("Name")) {
                    radioGroup.check(R.id.xml_sort_by_name);
                } else if (selected_sorting_type.equals("Date")) {
                    radioGroup.check(R.id.xml_sort_by_date);
                }


                //try to save the setting in shared pref
                //radioGroup.check(R.id.xml_sort_by_price);
                selected_radio = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) radioGroup.findViewById(selected_radio);

                //----------------------------------------------
                // Add the Listener to the RadioGroup
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // Get the selected Radio Button
                        radioButton = (RadioButton) group.findViewById(checkedId);

                        pref_editor.putString("SORT", radioButton.getText().toString());
                        pref_editor.commit();

                        selected_sorting_type = radioButton.getText().toString();
                        if (selected_sorting_type.equals("Price")) {
                            loaddata_byprice();
                        } else if (selected_sorting_type.equals("Name")) {
                            loaddata_byname();
                        } else if (selected_sorting_type.equals("Date")) {
                            loaddata_bydate();
                        }
                        sort_dialog.dismiss();

                    }
                });
                //----------------------------------------------
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Setting_Fragment()).addToBackStack("Setting").commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void average_Month_year_cal() {

        if(btm_avg_month_year_swtich.equals("Monthly")){
            average_usage_tv.setText("Per Month");
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            double temp_month = average_usage_month_year;
            average_usage.setText(curr_symbol +" "+ String.valueOf(decimalFormat.format(temp_month)));
            btm_avg_month_year_swtich = "Yearly";

        } else if (btm_avg_month_year_swtich.equals("Yearly")) {
            average_usage_tv.setText("Per Year");
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            double temp_year = average_usage_month_year * 12; // we have per month data already just convert it to Yearly
            average_usage.setText(curr_symbol +" "+ String.valueOf(decimalFormat.format(temp_year)));
            btm_avg_month_year_swtich = "Monthly";  //to handling next click
        }

    }

    private void average_Monthly_usage_cal() {

        btm_avg_month_year_swtich ="Yearly"; //to handling next click
        average_usage_tv.setText("Per Month");

        subs_viewModel.getAllSubs_list_by_price().observe(getViewLifecycleOwner(), subsLists -> {


            double price = 0;
            for(Subs_list item : subsLists){
                String bill_type = item.getBillingcycle_step_type();
                int bill_cycle_no = item.getBillingcycle_no(); // 1month once, 2month once

                if(bill_type.equals("Monthly")){
                    double mon_temp = item.getPrice();
                    price = price + (mon_temp/bill_cycle_no);
                }
                if(bill_type.equals("Daily")){
                    int daily_temp = 30 / bill_cycle_no;
                    price = price + (item.getPrice() * daily_temp); //monthly
                }
                if(bill_type.equals("Weekly")){

                    if(bill_cycle_no == 1){
                        price = price + (item.getPrice() * 4); //monthly
                    } else if (bill_cycle_no == 2 ) {
                        price = price + (item.getPrice() * 2); //monthly
                    } else if (bill_cycle_no == 3 || bill_cycle_no ==4) {
                        price = price + item.getPrice(); //monthly
                    } else if (bill_cycle_no > 4) {
                        int week_cycle_tmp = bill_cycle_no /4;
                        double week_temp = item.getPrice();
                        price = price + (week_temp/week_cycle_tmp);
                    }
                }
                if(bill_type.equals("Yearly")){
                        int yearly_temp = bill_cycle_no * 12;  //converting to no of months
                        price = price + (item.getPrice()/yearly_temp); //monthly
                }
            }
            DecimalFormat decimalFormat = new DecimalFormat("##.00"); // to keep the double from to 23.33333 to 23.33
            average_usage_month_year = price;
            average_usage.setText(curr_symbol +" "+ String.valueOf(decimalFormat.format(price)));
        });
    }

    private void loaddata_byprice() {

        subs_viewModel.getAllSubs_list_by_price().observe(getViewLifecycleOwner(), subsLists -> {

            if (subsLists != null && !subsLists.isEmpty()) {
                subs_lists = subsLists;
                R_adaper.set_sub_list((ArrayList<Subs_list>) subsLists);
                recyclerView.setAdapter(R_adaper);
                R_adaper.notifyDataSetChanged();

            }else{
                add_subscription.setVisibility(View.VISIBLE); // Prompt screen if no subs are added yet
                average_usage_lt.setVisibility(View.GONE);
            }
        });


    }

    private void loaddata_byname() {
        subs_viewModel.getAllSubsFromVm_by_name().observe(getViewLifecycleOwner(), subs_list ->
        {
            if (subs_list != null && !subs_list.isEmpty()) {
                subs_lists = subs_list;
                R_adaper.set_sub_list((ArrayList<Subs_list>) subs_list);
                recyclerView.setAdapter(R_adaper);
                R_adaper.notifyDataSetChanged();
            }else{
                add_subscription.setVisibility(View.VISIBLE); // Prompt screen if no subs are added yet
                average_usage_lt.setVisibility(View.GONE);
            }
        });
    }

    private void loaddata_bydate() {
        subs_viewModel.getAllSubs_list_by_date().observe(getViewLifecycleOwner(), subs_list ->
        {
            if (subs_list != null && !subs_list.isEmpty()) {
                subs_lists = subs_list;
                R_adaper.set_sub_list((ArrayList<Subs_list>) subs_list);
                recyclerView.setAdapter(R_adaper);
                R_adaper.notifyDataSetChanged();
            }else{
                add_subscription.setVisibility(View.VISIBLE); // Prompt screen if no subs are added yet
                average_usage_lt.setVisibility(View.GONE);
            }
        });
    }

}