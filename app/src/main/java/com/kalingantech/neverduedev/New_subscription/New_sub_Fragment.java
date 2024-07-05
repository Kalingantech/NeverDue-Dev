package com.kalingantech.neverduedev.New_subscription;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.Home.Home_Fragment;
import com.kalingantech.neverduedev.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class New_sub_Fragment extends Fragment {

    EditText price_edit, name_edit, notes_edit, link_edit, paymenttracking_edit, paymenttype_edit;
    EditText category_edit, profile_edit;
    TextView bilingcycle_startdate_tv;
    Button submit_btn;
    Uri selected_image_uri;
    String  selected_bilingcycle_type;
    int Selected_bilingcycle_no_edit;
    ImageView sub_image;

    Switch reminder_switch;
    Boolean selected_reminder_true_false = false;
    TextView edit_Reminder_edit_tv;
    int selected_reminder_day_count;


    private Subs_ViewModel subs_viewModel;
    DatePickerDialog.OnDateSetListener DatePickerDialog_return_method_date, DatePickerDialog_return_method_startdate;

    Calendar billing_Calendar = Calendar.getInstance();
    Calendar billing_Start_Calendar = Calendar.getInstance();

    Date biling_next_date, biling_start_date;
    NotificationManager notificationManager;
    Button notify_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_sub, container, false);

        //============================
        Bundle arguments = getArguments();
        int Image = arguments.getInt("Image");
        String name = arguments.getString("Name");
        int default_colour = arguments.getInt("Color");
        //============================

        sub_image = view.findViewById(R.id.xml_sub_image);
        price_edit = view.findViewById(R.id.xml_edit_price);
        name_edit = view.findViewById(R.id.xml_edit_name);
        notes_edit = view.findViewById(R.id.xml_edit_Description);
        bilingcycle_startdate_tv = view.findViewById(R.id.xml_edit_Billing_startdate);

        paymenttype_edit = view.findViewById(R.id.xml_edit_Paymentmethod);

        category_edit = view.findViewById(R.id.xml_edit_Category);
        profile_edit = view.findViewById(R.id.xml_edit_Profilename);
        reminder_switch = view.findViewById(R.id.xml_reminder_switch);
        edit_Reminder_edit_tv = view.findViewById(R.id.xml_edit_Reminder_data);

        submit_btn = view.findViewById(R.id.xml_submit_btn);
        sub_image.setImageResource(Image);
        selected_image_uri = getUri(Image);
        name_edit.setText(name);
        reminder_switch.setChecked(false);
        edit_Reminder_edit_tv.setEnabled(false);

        //createnotication();


        //---------------setting default values---------------
        biling_start_date = billing_Start_Calendar.getTime();
        biling_next_date = billing_Start_Calendar.getTime();
        String str_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(billing_Start_Calendar.getTime());
        bilingcycle_startdate_tv.setText(str_date);
        //---------------setting default values---------------

        //-------------------------spinner-------------//
        Spinner billing_value_spinner = view.findViewById(R.id.xml_edit_Billing_cycle_no);

        List<Integer> billing_values = new ArrayList<Integer>();

        for (int i = 1; i < 31; i++) {
            billing_values.add(i);
        }

        ArrayAdapter value_ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, billing_values);
        // set simple layout resource file for each item of spinner
        value_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billing_value_spinner.setAdapter(value_ad);

        billing_value_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Spinner billing_types_spinner = view.findViewById(R.id.xml_billingcycle_step_type);
        String[] billing_types = {"Monthly", "Daily", "Weekly", "Yearly"};
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


        subs_viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(Subs_ViewModel.class);


        sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image();
            }
        });

        bilingcycle_startdate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), DatePickerDialog_return_method_startdate, billing_Start_Calendar.get(Calendar.YEAR), billing_Start_Calendar.get(Calendar.MONTH), billing_Start_Calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog_return_method_startdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                billing_Start_Calendar.set(Calendar.YEAR, year);
                billing_Start_Calendar.set(Calendar.MONTH, month);
                billing_Start_Calendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MMM/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
                String str_date = dateFormat.format(billing_Start_Calendar.getTime());

                biling_start_date = billing_Start_Calendar.getTime();
                bilingcycle_startdate_tv.setText(str_date);

            }
        };


        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_permmissons();
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


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal_ref = Calendar.getInstance();
                if (billing_Start_Calendar.after(cal_ref)) {
                    biling_next_date = billing_Start_Calendar.getTime();
                } else if (billing_Start_Calendar.before(cal_ref) || billing_Start_Calendar.equals(cal_ref)) {
                    biling_next_date = getfuturedate(billing_Start_Calendar.getTime(), Selected_bilingcycle_no_edit, selected_bilingcycle_type);
                }


                Subs_list subs_list = new Subs_list(
                        selected_image_uri.toString(), //
                        Double.parseDouble(price_edit.getText().toString()),
                        Double.parseDouble(price_edit.getText().toString()),//this is total amount paid, initially setting to default amount
                        name_edit.getText().toString(),
                        //----------------------------//
                        biling_next_date,
                        biling_start_date,
                        Selected_bilingcycle_no_edit,
                        selected_bilingcycle_type,
                        //----------------------------//
                        selected_reminder_true_false,
                        selected_reminder_day_count,
                        //----------------------------//
                        notes_edit.getText().toString(),
                        paymenttype_edit.getText().toString(),
                        category_edit.getText().toString(),
                        profile_edit.getText().toString());

                subs_viewModel.insertsubs_list(subs_list);

                returntohome();
            }
        });

        return view;

    }

    private void check_permmissons() {

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        } else{
            edit_Reminder_edit_tv.setEnabled(true);
            selected_reminder_true_false = true;
            reminder_switch.setChecked(true);
        }

        set_Alarm();

    }

    private void set_Alarm() {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            //if permission is denied
            Toast.makeText(getContext(),"Denied",Toast.LENGTH_SHORT).show();
            reminder_switch.setChecked(false);
            edit_Reminder_edit_tv.setEnabled(false);
            selected_reminder_true_false = false;
        }else{
            //if permission is granter
            edit_Reminder_edit_tv.setEnabled(true);
            selected_reminder_true_false = true;
        }

        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createnotication() {



    }

    private void getreminderdata() {

        final Dialog curr_dialog = new Dialog(getContext());
        curr_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        curr_dialog.setContentView(R.layout.reminder_data_picker);
        curr_dialog.show();
        curr_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = curr_dialog.findViewById(R.id.xml_reminder_data_list);
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


        int logos[] = {R.drawable.bike, R.drawable.home, R.drawable.tv, R.drawable.car,
                R.drawable.baseline_adb_24, };

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


    private Date getfuturedate(Date date, int billingcycleNo, String type) {

        Date return_data = null;

        if (type.equals("Daily")) {
            Calendar daily_cal = Calendar.getInstance();
            daily_cal.setTime(date);
            daily_cal.add(Calendar.DATE, billingcycleNo);
            return_data = daily_cal.getTime();

        } else if (type.equals("Weekly")) {
            Calendar weekly_cal = Calendar.getInstance();
            weekly_cal.setTime(date);
            weekly_cal.add(Calendar.DATE, billingcycleNo + 6);
            return_data = weekly_cal.getTime();

        } else if (type.equals("Monthly")) {
            Calendar Monthy_cal = Calendar.getInstance();
            Monthy_cal.setTime(date);
            Monthy_cal.add(Calendar.MONTH, billingcycleNo);
            return_data = Monthy_cal.getTime();

        } else if (type.equals("Yearly")) {
            Calendar Yearly_cal = Calendar.getInstance();
            Yearly_cal.setTime(date);
            Yearly_cal.add(Calendar.YEAR, billingcycleNo);
            return_data = Yearly_cal.getTime();
        }

        return return_data;

    }

    private static Uri getUri(int res) {
        return Uri.parse("android.resource://com.kalingantech.neverduedev/" + res);
    }

    private void returntohome() {

        getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Home_Fragment()).commit();

    }


}