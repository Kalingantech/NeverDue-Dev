package com.kalingantech.neverduedev.New_subscription;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.Home.Home_Fragment;
import com.kalingantech.neverduedev.R;
import com.kalingantech.neverduedev.Utils.Category_logo_model;
import com.kalingantech.neverduedev.Utils.Category_picker_Adapter;
import com.kalingantech.neverduedev.Utils.Icon_Adapter;
import com.kalingantech.neverduedev.Utils.Pay_method_Adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Edit_sub_Fragment extends Fragment {

    EditText price_edit, name_edit, notes_edit, link_edit, paymenttracking_edit;
    EditText  profile_edit, reminder_edit, remindertype_edit;
    TextView biling_next_date_tv, bilingcycle_startdate_tv,category_tv,paymenttype_tv;
    Button colour_btn, update_btn;
    Uri selected_image_uri;
    String selected_curr_logo, selected_curr, selected_bilingcycle_type,selected_category,selected_pay_method;
    int Selected_bilingcycle_no_edit;
    ImageView sub_image;

    Switch reminder_switch;
    Boolean selected_reminder_true_false;
    TextView edit_Reminder_edit_tv,paymentmethod_tv;
    int selected_reminder_day_count;
    String Reminder_return_value;
    private Subs_ViewModel subs_viewModel;
    DatePickerDialog.OnDateSetListener DatePickerDialog_return_method_next_date, DatePickerDialog_return_method_start_date;

    Spinner billing_types_spinner, Billing_cycle_spinner;
    Subs_list edit_subs_list;
    //Calendar billing_next_Calendar = Calendar.getInstance();
    Calendar billing_Start_Calendar = Calendar.getInstance();

    Date biling_next_date, biling_start_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_sub, container, false);

        //============================
        Bundle arguments = getArguments();
        int UID = arguments.getInt("UID");
        //============================


        sub_image = view.findViewById(R.id.xml_sub_image);
        price_edit = view.findViewById(R.id.xml_edit_price);
        name_edit = view.findViewById(R.id.xml_edit_name);

        notes_edit = view.findViewById(R.id.xml_edit_Description);
        biling_next_date_tv = view.findViewById(R.id.xml_Billing_next_Date);
        Billing_cycle_spinner = view.findViewById(R.id.xml_edit_Billing_cycle_no);
        bilingcycle_startdate_tv = view.findViewById(R.id.xml_edit_Billing_startdate);

        paymentmethod_tv = view.findViewById(R.id.xml_Paymentmethod);

        category_tv = view.findViewById(R.id.xml_category_tv);
        profile_edit = view.findViewById(R.id.xml_edit_Profilename);
        reminder_switch = view.findViewById(R.id.xml_reminder_switch);
        edit_Reminder_edit_tv = view.findViewById(R.id.xml_edit_Reminder_data);

        update_btn = view.findViewById(R.id.xml_update_btn);

        subs_viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(Subs_ViewModel.class);
        subs_viewModel.getSearch_list_by_uid(UID).observe(getViewLifecycleOwner(), subs_list -> {

            edit_subs_list = subs_list;
            sub_image.setImageURI(Uri.parse(subs_list.getSub_image()));
            selected_image_uri = Uri.parse(subs_list.getSub_image());

            price_edit.setText(String.valueOf(subs_list.getPrice()));
            name_edit.setText(subs_list.getName());

            biling_next_date = subs_list.getBilling_next_date();
            String start_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(subs_list.getBilling_next_date().getTime());

            biling_start_date = subs_list.getBilling_start_date();
            String end_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(subs_list.getBilling_start_date().getTime());

            biling_next_date_tv.setText(start_date);
            bilingcycle_startdate_tv.setText(end_date);

            int Billingcycle_no = subs_list.getBillingcycle_no();
            for (int i = 0; i < Billing_cycle_spinner.getCount(); i++) {
                if (Billing_cycle_spinner.getItemAtPosition(i).equals(Billingcycle_no)) {
                    Billing_cycle_spinner.setSelection(i);
                    break;
                }
            }

            String temp_spinner_type = subs_list.getBillingcycle_step_type().toString();
            for (int i = 0; i < billing_types_spinner.getCount(); i++) {
                if (billing_types_spinner.getItemAtPosition(i).equals(temp_spinner_type)) {
                    billing_types_spinner.setSelection(i);
                    break;
                }
            }

            reminder_switch.setChecked(subs_list.getReminder());
            selected_reminder_true_false = subs_list.getReminder();


            if (subs_list.getReminder().equals(true)) {
                selected_reminder_day_count = subs_list.getReminder_no();
                String temp_rem_steps = get_reminder_days(subs_list.getReminder_no());
                edit_Reminder_edit_tv.setText(temp_rem_steps);
            } else {
                edit_Reminder_edit_tv.setEnabled(false);
            }

            notes_edit.setText(subs_list.getNotes());
            //reminder_edit.setText(subs_list.getReminder());
            //remindertype_edit.setText(subs_list.getRemindertype());
            paymentmethod_tv.setText(subs_list.getPaymenttype());
            selected_pay_method = subs_list.getPaymenttype();

            category_tv.setText(subs_list.getCategory());
            selected_category = subs_list.getCategory();

            profile_edit.setText(subs_list.getProfile());


        });


        //-------------------------spinner-------------//
        List<Integer> billing_values = new ArrayList<Integer>();
        for (int i = 1; i < 31; i++) {
            billing_values.add(i);
        }

        ArrayAdapter value_ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, billing_values);
        // set simple layout resource file for each item of spinner
        value_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Billing_cycle_spinner.setAdapter(value_ad);

        Billing_cycle_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), , Toast.LENGTH_LONG).show();

                Selected_bilingcycle_no_edit = billing_values.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Selected_bilingcycle_no_edit = 1;
            }
        });
        //-------------------------spinner-------------//
        //-------------------------spinner-------------//
        billing_types_spinner = view.findViewById(R.id.xml_billingcycle_step_type);
        String[] billing_types = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter spin_ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, billing_types);
        // set simple layout resource file for each item of spinner
        spin_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billing_types_spinner.setAdapter(spin_ad);

        billing_types_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), , Toast.LENGTH_LONG).show();

                selected_bilingcycle_type = billing_types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_bilingcycle_type = "Monthly";
            }
        });
        //-------------------------spinner-------------//


        sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image();
            }
        });

        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // on below line we are setting text if switch is checked.
                    edit_Reminder_edit_tv.setEnabled(true);
                    selected_reminder_true_false = true;
                } else {
                    // on below line we are setting text if switch is NOT checked.
                    edit_Reminder_edit_tv.setEnabled(false);
                    selected_reminder_true_false = false;
                }
            }
        });

        edit_Reminder_edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getreminderdata();
            }
        });

        category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categorydata();
            }
        });

        paymentmethod_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pay_methoddata();
            }
        });



        /*biling_next_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), DatePickerDialog_return_method_next_date, billing_next_Calendar.get(Calendar.YEAR), billing_next_Calendar.get(Calendar.MONTH), billing_next_Calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        DatePickerDialog_return_method_next_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                billing_next_Calendar.set(Calendar.YEAR, year);
                billing_next_Calendar.set(Calendar.MONTH, month);
                billing_next_Calendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MMM/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
                String str_date = dateFormat.format(billing_next_Calendar.getTime());


                biling_next_date_tv.setText(str_date);
                biling_next_date = billing_next_Calendar.getTime();
            }
        };
        bilingcycle_startdate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "reached", Toast.LENGTH_SHORT).show();
                new DatePickerDialog(getContext(), DatePickerDialog_return_method_start_date, billing_Start_Calendar.get(Calendar.YEAR), billing_Start_Calendar.get(Calendar.MONTH), billing_Start_Calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog_return_method_start_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                billing_Start_Calendar.set(Calendar.YEAR, year);
                billing_Start_Calendar.set(Calendar.MONTH, month);
                billing_Start_Calendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MMM/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
                String str_date = dateFormat.format(billing_Start_Calendar.getTime());
                bilingcycle_startdate_tv.setText(str_date);
                biling_start_date = billing_Start_Calendar.getTime();
            }
        };*/


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                edit_subs_list.setSub_image(selected_image_uri.toString());
                edit_subs_list.setPrice(Double.parseDouble(price_edit.getText().toString()));
                edit_subs_list.setName(name_edit.getText().toString());

                edit_subs_list.setBilling_next_date(biling_next_date);
                edit_subs_list.setBillingcycle_no(Selected_bilingcycle_no_edit);
                edit_subs_list.setBillingcycle_step_type(selected_bilingcycle_type);
                edit_subs_list.setBilling_start_date(biling_start_date);


                edit_subs_list.setNotes(notes_edit.getText().toString());
                edit_subs_list.setReminder(selected_reminder_true_false);
                edit_subs_list.setReminder_no(selected_reminder_day_count);
                //edit_subs_list.setReminder(reminder_edit.getText().toString());
                //edit_subs_list.setRemindertype(remindertype_edit.getText().toString());
                //---------------------------
                edit_subs_list.setPaymenttype(selected_pay_method);
                edit_subs_list.setCategory(selected_category);
                edit_subs_list.setProfile(profile_edit.getText().toString());

                //total amount we are not updating it here.


                subs_viewModel.update_sub_rep(edit_subs_list);

                //------------
                returntohome();
                //------------

            }
        });


        return view;

    }
    private void select_image() {

        final Dialog img_dialog = new Dialog(getContext());
        img_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        img_dialog.setContentView(R.layout.icon_picer);
        img_dialog.show();
        img_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //dialog.getWindow().setGravity(Gravity.BOTTOM);

        GridView gridView = img_dialog.findViewById(R.id.xml_icon_gridvw);


        int logos[] = {R.drawable.baseline_adb_24, R.drawable.baseline_android_24, R.drawable.baseline_adb_24, R.drawable.baseline_android_24,
                R.drawable.baseline_adb_24, R.drawable.baseline_android_24, R.drawable.baseline_adb_24};

        Icon_Adapter icon_adapter = new Icon_Adapter(img_dialog.getContext(), logos);
        gridView.setAdapter(icon_adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sub_image.setImageResource(logos[position]);
                selected_image_uri = getUri(logos[position]);

                img_dialog.dismiss();
            }
        });

    }

    public static Uri getUri(int res) {
        return Uri.parse("android.resource://com.kalingantech.neverduedev/" + res);
    }

    private void pay_methoddata() {

        final Dialog pay_dialog = new Dialog(getContext());
        pay_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pay_dialog.setContentView(R.layout.payment_list_data_picker);
        pay_dialog.show();
        pay_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = pay_dialog.findViewById(R.id.xml_simple_data_listvw);

        ArrayList<String> pay_method_list = new ArrayList<>();


        pay_method_list.add("Debit card");
        pay_method_list.add("Credit card");
        pay_method_list.add("Cash");
        pay_method_list.add("Google Pay");
        pay_method_list.add("Amazon Pay");
        pay_method_list.add("Other");


        Pay_method_Adapter simple_Adapter = new Pay_method_Adapter(pay_dialog.getContext(), pay_method_list);
        listView.setAdapter(simple_Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_pay_method = pay_method_list.get(position).toString();
                paymentmethod_tv.setText(pay_method_list.get(position).toString());
                pay_dialog.dismiss();
            }
        });
    }

    private void categorydata() {

        final Dialog cat_dialog = new Dialog(getContext());
        cat_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cat_dialog.setContentView(R.layout.category_picker);
        cat_dialog.show();
        cat_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = cat_dialog.findViewById(R.id.xml_cat_data_listvw);

        ArrayList<Category_logo_model> catergory_list = new ArrayList<>();

        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Education"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Food"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Gaming"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Grocery"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Health"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Insurace"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Internet"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Entertainment"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Sports"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Transportation"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24,"Other"));

        Category_picker_Adapter simple_Adapter = new Category_picker_Adapter(cat_dialog.getContext(), catergory_list);
        listView.setAdapter(simple_Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_category = catergory_list.get(position).getCat_name();
                category_tv.setText(catergory_list.get(position).getCat_name());
                cat_dialog.dismiss();
            }
        });

    }
    private void getreminderdata() {

        final Dialog curr_dialog = new Dialog(getContext());
        curr_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        curr_dialog.setContentView(R.layout.payment_list_data_picker);
        curr_dialog.show();
        curr_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = curr_dialog.findViewById(R.id.xml_simple_data_listvw);
        ArrayList<Reminder_model> reminderModels = new ArrayList<>();
        reminderModels.add(new Reminder_model(0, "Same day"));
        reminderModels.add(new Reminder_model(1, "One day"));
        reminderModels.add(new Reminder_model(2, "Two days"));
        reminderModels.add(new Reminder_model(7, "One Week"));
        reminderModels.add(new Reminder_model(30, "One Month"));

        Reminder_picker_Adapter reminder_Adapter = new Reminder_picker_Adapter(curr_dialog.getContext(), reminderModels);
        listView.setAdapter(reminder_Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_reminder_day_count = reminderModels.get(position).days;
                edit_Reminder_edit_tv.setText(reminderModels.get(position).string_days);
                curr_dialog.dismiss();
            }
        });
    }

    private String get_reminder_days(int value_days) {


        ArrayList<Reminder_model> reminderModels = new ArrayList<>();
        reminderModels.add(new Reminder_model(0, "Same day"));
        reminderModels.add(new Reminder_model(1, "One day"));
        reminderModels.add(new Reminder_model(2, "Two days"));
        reminderModels.add(new Reminder_model(7, "One Week"));
        reminderModels.add(new Reminder_model(14, "Two Weeks"));
        reminderModels.add(new Reminder_model(30, "One Month"));
        for (int i = 0; i < reminderModels.size(); i++) {
            if (value_days == reminderModels.get(i).days) {
                Reminder_return_value = reminderModels.get(i).getString_days();
            } else {
                //do nothing if doesnt match
            }
        }
        return Reminder_return_value;
    }

    private void returntohome() {

        getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Home_Fragment()).commit();
    }


}