package com.kalingantech.neverduedev.New_subscription;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalingantech.neverduedev.DB.Profile_list;
import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.DB.Subs_list;
import com.kalingantech.neverduedev.Home.Home_Fragment;
import com.kalingantech.neverduedev.R;
import com.kalingantech.neverduedev.Utils.Category_logo_model;
import com.kalingantech.neverduedev.Utils.Category_picker_Adapter;
import com.kalingantech.neverduedev.Utils.Icon_Adapter;
import com.kalingantech.neverduedev.Utils.Notification_new;
import com.kalingantech.neverduedev.Utils.Pay_method_Adapter;
import com.kalingantech.neverduedev.Utils.Profile_list_Adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class New_sub_Fragment extends Fragment {

    EditText price_edit, name_edit, notes_edit, link_edit, paymenttracking_edit, paymenttype_edit, temp_min;
    TextView billing_cycle_tv,bilingcycle_startdate_tv, billing_nextdate_tv, category_tv, paymentmethod_tv, profilename_tv;
    Button submit_btn;
    Uri selected_image_uri;
    String SELECTED_SUB_NAME, selected_bilingcycle_type, selected_category, selected_pay_method, selected_profile, NEW_PROFILE_NAME;
    int Selected_bilingcycle_no_edit;
    ImageView sub_image;

    Switch reminder_switch;
    Boolean selected_reminder_true_false = false;
    TextView edit_Reminder_edit_tv;
    int selected_reminder_day_count, random_notify_code;


    private Subs_ViewModel subs_viewModel;
    DatePickerDialog.OnDateSetListener DatePickerDialog_return_method_date, DatePickerDialog_return_method_startdate;


    Calendar billing_Start_Calendar = Calendar.getInstance();
    Calendar billing_next_Calendar = Calendar.getInstance();
    Date biling_next_date, biling_start_date;
    NotificationManager notificationManager;
    Button notify_btn;

    Profile_list_Adapter profile_list_adapter;

    ArrayList<Profile_list> pro_lists = new ArrayList<Profile_list>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor pref_editor;

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

        subs_viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(Subs_ViewModel.class);

        sub_image = view.findViewById(R.id.xml_sub_image);
        price_edit = view.findViewById(R.id.xml_edit_price);
        temp_min = view.findViewById(R.id.xml_temp_min);
        name_edit = view.findViewById(R.id.xml_edit_name);

        ConstraintLayout billing_cycle_lt = view.findViewById(R.id.xml_edit_billing_cycle_lt);
        billing_cycle_tv =view.findViewById(R.id.xml_edit_billing_cycle_tv);



        ConstraintLayout billing_startdate_lt = view.findViewById(R.id.xml_edit_Billing_startdate_lt);
        bilingcycle_startdate_tv = view.findViewById(R.id.xml_edit_Billing_startdate);
        billing_nextdate_tv = view.findViewById(R.id.xml_edit_Billing_nextdate);

        ConstraintLayout category_tv_lt = view.findViewById(R.id.xml_edit_Category_lt);
        category_tv = view.findViewById(R.id.xml_edit_Category);

        ConstraintLayout paymentmethod_tv_lt = view.findViewById(R.id.xml_Paymentmethod_lt);
        paymentmethod_tv = view.findViewById(R.id.xml_Paymentmethod);

        ConstraintLayout profilename_tv_lt = view.findViewById(R.id.xml_Profilename_lt);
        profilename_tv = view.findViewById(R.id.xml_Profilename_tv);

        ConstraintLayout reminder_switch_lt =view.findViewById(R.id.xml_reminder_switch_lt);
        reminder_switch = view.findViewById(R.id.xml_reminder_switch);

        ConstraintLayout reminder_data_lt =view.findViewById(R.id.xml_edit_reminder_data_lt);
        edit_Reminder_edit_tv = view.findViewById(R.id.xml_edit_Reminder_data);


        notes_edit = view.findViewById(R.id.xml_edit_Description);
        submit_btn = view.findViewById(R.id.xml_submit_btn);




        sub_image.setImageResource(Image);
        selected_image_uri = getUri(Image);
        name_edit.setText(name);
        Selected_bilingcycle_no_edit = 1;//default
        selected_bilingcycle_type = "Monthly"; //default
        billing_cycle_tv.setText("Monthly Once");


        reminder_switch.setChecked(false);
        reminder_data_lt.setEnabled(false);


        //createnotication();


        sharedPreferences = getActivity().getSharedPreferences("PREF", 0);
        pref_editor = sharedPreferences.edit();


        //---------------setting default values---------------
        biling_start_date = billing_Start_Calendar.getTime();
        String str_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(billing_Start_Calendar.getTime());
        bilingcycle_startdate_tv.setText(str_date);

        billing_next_Calendar.add(Calendar.MONTH, 1); //since default increment is 1 month once.
        biling_next_date = billing_next_Calendar.getTime();
        String next_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(billing_next_Calendar.getTime());
        billing_nextdate_tv.setText(next_date);

        //---------------setting default values---------------

        billing_cycle_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingcycle_data();
            }
        });

        sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image();
            }
        });

        billing_startdate_lt.setOnClickListener(new View.OnClickListener() {
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

                //getting future date
                billing_next_Calendar = getfuturedate(billing_Start_Calendar.getTime(), Selected_bilingcycle_no_edit, selected_bilingcycle_type);
                biling_next_date = billing_next_Calendar.getTime();
                String next_date = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault()).format(billing_next_Calendar.getTime());
                billing_nextdate_tv.setText(next_date);
            }
        };
        reminder_switch_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(reminder_switch.isChecked()){
                    reminder_switch.setChecked(false);
                } else if (!reminder_switch.isChecked()) {
                    reminder_switch.setChecked(true);
                }

            }
        });
        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_permmissons();
                    reminder_data_lt.setEnabled(true);
                    selected_reminder_true_false = true;
                } else {
                    // on below line we are setting text if switch is NOT checked.
                    reminder_data_lt.setEnabled(false);
                    selected_reminder_true_false = false;
                }
            }
        });

        reminder_data_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getreminderdata();
            }
        });

        paymentmethod_tv_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_methoddata();
            }
        });
        category_tv_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categorydata();
            }
        });

        profilename_tv_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile_data();
            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SELECTED_SUB_NAME = name_edit.getText().toString();


               /* Calendar cal_ref = Calendar.getInstance();
                Calendar alarm_cal = Calendar.getInstance();

                if (billing_Start_Calendar.after(cal_ref)) {
                    alarm_cal = billing_Start_Calendar;
                    biling_next_date = billing_Start_Calendar.getTime();

                } else if (billing_Start_Calendar.before(cal_ref) || billing_Start_Calendar.equals(cal_ref)) {
                    alarm_cal = getfuturedate(billing_Start_Calendar.getTime(), Selected_bilingcycle_no_edit, selected_bilingcycle_type);

                    biling_next_date = alarm_cal.getTime();

                }*/

                if (selected_reminder_true_false.equals(true)) {


                    // create instance of Random class
                    Random random = new Random();

                    // Generate random integers in range 0 to 999, this will be used as notification code for each subscription
                    random_notify_code = random.nextInt(1000);


                    Calendar alarm_ref = Calendar.getInstance();
                    //Calendar alarm_ref = Calendar.getInstance();
                    int selected_alarm_hour = sharedPreferences.getInt("NOTIFICATION_HOUR", 18);

                    alarm_ref.set(Calendar.YEAR, billing_next_Calendar.get(Calendar.YEAR));
                    alarm_ref.set(Calendar.MONTH, billing_next_Calendar.get(Calendar.MONTH));
                    alarm_ref.set(Calendar.DAY_OF_MONTH, billing_next_Calendar.get(Calendar.DAY_OF_MONTH));
                    alarm_ref.set(Calendar.HOUR_OF_DAY, selected_alarm_hour);
                    alarm_ref.set(Calendar.MINUTE, Integer.parseInt(temp_min.getText().toString()));
                    alarm_ref.set(Calendar.SECOND, 00);

                    schedule_Alarm(SELECTED_SUB_NAME, random_notify_code, alarm_ref.getTimeInMillis());
                }


                Subs_list subs_list = new Subs_list(
                        selected_image_uri.toString(), //
                        Double.parseDouble(price_edit.getText().toString()),
                        Double.parseDouble(price_edit.getText().toString()),//this is total amount paid, initially setting to default amount
                        SELECTED_SUB_NAME,
                        //----------------------------//
                        biling_next_date,
                        biling_start_date,
                        Selected_bilingcycle_no_edit,
                        selected_bilingcycle_type,
                        //----------------------------//
                        selected_reminder_true_false,
                        selected_reminder_day_count,
                        random_notify_code,
                        //----------------------------//
                        notes_edit.getText().toString(),
                        selected_pay_method,
                        selected_category,
                        selected_profile);

                subs_viewModel.insertsubs_list(subs_list);

                returntohome();
            }
        });


        return view;

    }

    private void billingcycle_data() {

        final Dialog bill_cycle_dialog = new Dialog(getContext());
        bill_cycle_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bill_cycle_dialog.setContentView(R.layout.billing_cycle_picker);
        bill_cycle_dialog.show();
        bill_cycle_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Spinner billing_value_spinner = bill_cycle_dialog.findViewById(R.id.xml_edit_Billing_cycle_no);
        Spinner billing_types_spinner = bill_cycle_dialog.findViewById(R.id.xml_billingcycle_step_type);
        Button save = bill_cycle_dialog.findViewById(R.id.xml_save_cycle);

        //-------------------------spinner-------------//
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Selected_bilingcycle_no_edit == 1){
                    String temp_data = selected_bilingcycle_type+" "+"Once";
                    billing_cycle_tv.setText(temp_data);
                    bill_cycle_dialog.dismiss();
                }else {
                    String temp_data = selected_bilingcycle_type+" "+Selected_bilingcycle_no_edit+" "+"times";
                    billing_cycle_tv.setText(temp_data);
                    bill_cycle_dialog.dismiss();
                }
            }
        });
    }

    private void profile_data() {
        final Dialog profile_dialog = new Dialog(getContext());
        profile_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profile_dialog.setContentView(R.layout.profile_list_data_picker);
        profile_dialog.show();
        profile_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageButton add_new = profile_dialog.findViewById(R.id.xml_add_new_profile);
        LinearLayout layout = profile_dialog.findViewById(R.id.foldable_layout);
        EditText name_ed = profile_dialog.findViewById(R.id.xml_new_profile_edit);
        Button button = profile_dialog.findViewById(R.id.xml_new_profile_save);

        RecyclerView recyclerView = profile_dialog.findViewById(R.id.xml_profile_data_listvw);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(profile_dialog.getContext()));


        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NEW_PROFILE_NAME = name_ed.getText().toString().trim();

                        if (NEW_PROFILE_NAME.isEmpty()) {
                            Toast.makeText(profile_dialog.getContext(), "Enter a validate name", Toast.LENGTH_SHORT).show();
                        } else {
                            Profile_list name = new Profile_list(NEW_PROFILE_NAME);
                            subs_viewModel.insert_profile(name);
                            layout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        subs_viewModel.get_all_profiles().observe(getViewLifecycleOwner(), profileLists -> {
            pro_lists = (ArrayList<Profile_list>) profileLists;
            profile_list_adapter = new Profile_list_Adapter(profile_dialog.getContext(), pro_lists);
            recyclerView.setAdapter(profile_list_adapter);

            profile_list_adapter.setonItemclicklisterner(new Profile_list_Adapter.single_item_click_interface() {
                @Override
                public void onitemclick(String prof_name) {
                    profilename_tv.setText(prof_name);
                    selected_profile = prof_name;
                    profile_dialog.dismiss();
                }
            });

        });


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

        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Education"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Food"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Gaming"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Grocery"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Health"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Insurace"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Internet"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Entertainment"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Sports"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Transportation"));
        catergory_list.add(new Category_logo_model(R.drawable.baseline_adb_24, "Other"));

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

    private void check_permmissons() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        } else {
            edit_Reminder_edit_tv.setEnabled(true);
            selected_reminder_true_false = true;
            reminder_switch.setChecked(true);
        }


    }

    private void schedule_Alarm(String sub_name, int NOTIFICATION_CODE, long alarm_time) {

        String CHANNEL_ID = "CHANNEL_ID";
        //setting the Notify builder

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);

            if (notificationChannel == null) {
                CharSequence channel_name = "Channel 1";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
                notificationChannel.setDescription("Description");
                notificationManager.createNotificationChannel(notificationChannel);
            }

        } else {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            CharSequence channel_name = "Channel 1";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            notificationChannel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //create intent and attach it ot builder
        Intent intent_new = new Intent(getActivity().getApplicationContext(), Notification_new.class);
        intent_new.putExtra("NOTIFICATION_CODE", NOTIFICATION_CODE);
        intent_new.putExtra("NOTIFICATION_NAME", sub_name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), NOTIFICATION_CODE, intent_new, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm_time, pendingIntent);

        Toast.makeText(getContext(), String.valueOf(alarm_time), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF", 0);
        SharedPreferences.Editor pref_editor = sharedPreferences.edit();
        pref_editor.putLong("ALARM", alarm_time);
        pref_editor.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            //if permission is denied
            Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
            reminder_switch.setChecked(false);
            edit_Reminder_edit_tv.setEnabled(false);
            selected_reminder_true_false = false;
        } else {
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
                R.drawable.baseline_adb_24,};

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


    private Calendar getfuturedate(Date date, int billingcycleNo, String type) {

        Calendar return_data = null;

        if (type.equals("Daily")) {
            Calendar daily_cal = Calendar.getInstance();
            daily_cal.setTime(date);
            daily_cal.add(Calendar.DATE, billingcycleNo);
            return_data = daily_cal;

        } else if (type.equals("Weekly")) {
            Calendar weekly_cal = Calendar.getInstance();
            weekly_cal.setTime(date);
            weekly_cal.add(Calendar.DATE, billingcycleNo + 6);
            return_data = weekly_cal;

        } else if (type.equals("Monthly")) {
            Calendar Monthy_cal = Calendar.getInstance();
            Monthy_cal.setTime(date);
            Monthy_cal.add(Calendar.MONTH, billingcycleNo);
            return_data = Monthy_cal;

        } else if (type.equals("Yearly")) {
            Calendar Yearly_cal = Calendar.getInstance();
            Yearly_cal.setTime(date);
            Yearly_cal.add(Calendar.YEAR, billingcycleNo);
            return_data = Yearly_cal;
        }

        return return_data;

    }

    /*

    //--------------backups-------

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

    //--------------backups-------
*/
    private static Uri getUri(int res) {
        return Uri.parse("android.resource://com.kalingantech.neverduedev/" + res);
    }

    private void returntohome() {

        getFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Home_Fragment()).commit();

    }


}