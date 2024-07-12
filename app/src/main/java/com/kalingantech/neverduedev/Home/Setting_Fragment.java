package com.kalingantech.neverduedev.Home;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalingantech.neverduedev.R;
import com.kalingantech.neverduedev.Utils.Currency_logo_model;
import com.kalingantech.neverduedev.Utils.Currency_picker_Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Setting_Fragment extends Fragment {

    String selected_curr_logo, selected_curr_code, selected_language, temp_lang_code;

    TextView edit_currency, set_language_tv,notification_time_tv;

    int selected_alarm_hour;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor pref_editor;
    ArrayList<Currency_logo_model> currencyModels;

    NotificationManager notificationManager;

    RecyclerView currency_RcView;

    Currency_picker_Adapter curr_Adapter;

    Locale set_lang_locale;

    ConstraintLayout currency_lt, language_lt,notification_lt, policy_lt, contact_lt,rating_lt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        // Inflate the layout for this fragment


        currency_lt = view.findViewById(R.id.xml_currency_lt);
        edit_currency = view.findViewById(R.id.xml_edit_currency);

        language_lt = view.findViewById(R.id.xml_language_lt);
        set_language_tv = view.findViewById(R.id.xml_set_language_tv);

        notification_lt = view.findViewById(R.id.xml_notification_lt);
        notification_time_tv = view.findViewById(R.id.xml_notification_time_tv);

        policy_lt = view.findViewById(R.id.xml_policy_lt);
        contact_lt = view.findViewById(R.id.xml_contact_lt);
        rating_lt = view.findViewById(R.id.xml_rating_lt);

        sharedPreferences = getActivity().getSharedPreferences("PREF", 0);
        pref_editor = sharedPreferences.edit();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.xml_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button


        //Currency us = Currency.getInstance(getResources().getConfiguration().getLocales().get(0)); //-gives locale means en_IN for India
        //Currency us = Currency.getInstance(Locale.getDefault());

        selected_curr_logo = sharedPreferences.getString("CURRENCY_LOGO", "");
        selected_curr_code = sharedPreferences.getString("CURRENCY", "");
        selected_language = sharedPreferences.getString("LANGUAGE", "");
        selected_alarm_hour = sharedPreferences.getInt("NOTIFICATION_HOUR", 18);

        String concat = selected_curr_logo + " " + selected_curr_code;
        edit_currency.setText(concat);
        notification_time_tv.setText(String.valueOf(selected_alarm_hour));



        if (selected_language.equals("en")) {
            set_language_tv.setText("English");
        } else if (selected_language.equals("ta")) {
            set_language_tv.setText("தமிழ்");
        }

        currency_lt.setClickable(true);

        currency_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currency_picker();
            }
        });

        language_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language_picker();
            }
        });

        notification_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_time_picker();
            }
        });

        policy_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/kalingantech/home"));
                startActivity(browserIntent);
            }
        });

        contact_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_us();
            }
        });

        rating_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMarket();
            }
        });

        return view;
    }

    private void notification_time_picker() {
        final Dialog notify_dialog = new Dialog(getContext());
        notify_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        notify_dialog.setContentView(R.layout.setting_notification_time_picker);
        notify_dialog.show();
        notify_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Spinner spinner = notify_dialog.findViewById(R.id.xml_default_time_spinner);
        Button save_btn =notify_dialog.findViewById(R.id.xml_notify_time_save);





        List<Integer> default_time_values = new ArrayList<Integer>();

        for (int i = 1; i < 23; i++) {
            default_time_values.add(i);
        }

        ArrayAdapter value_ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, default_time_values);
        // set simple layout resource file for each item of spinner
        value_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(value_ad);


        int temp_time_hrs = sharedPreferences.getInt("NOTIFICATION_HOUR", 18);


        //setting the spinner value
        for (int i = 0; i < default_time_values.size(); i++) {
            if (spinner.getItemAtPosition(i).equals(temp_time_hrs)) {
                spinner.setSelection(i);
                break;
            }else{
                //DO_NOTHING
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), , Toast.LENGTH_LONG).show();

                selected_alarm_hour = default_time_values.get(position);
                notification_time_tv.setText(default_time_values.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_alarm_hour = 18; //18 hours
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref_editor.putInt("NOTIFICATION_HOUR", selected_alarm_hour);
                pref_editor.commit();
                notify_dialog.dismiss();
            }
        });


    }

    private void contact_us() {
        final Dialog contact_dialog = new Dialog(getContext());
        contact_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contact_dialog.setContentView(R.layout.setting_contact_us);
        contact_dialog.show();
        contact_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageButton telegram = contact_dialog.findViewById(R.id.xml_telegram);
        ImageButton mail_us = contact_dialog.findViewById(R.id.xml_mail_us);
        Button cancel = contact_dialog.findViewById(R.id.xml_cancel);

        mail_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + "kalingantech@gmail.com")); // to mail address
                intent.putExtra(Intent.EXTRA_EMAIL, "");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Never Due Help");
                startActivity(intent);
            }
        });

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+XT3nQztUFdl5ZTE1"));
                startActivity(browserIntent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_dialog.dismiss();
            }
        });


    }

    private void language_picker() {
        final Dialog lang_dialog = new Dialog(getContext());
        lang_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lang_dialog.setContentView(R.layout.language_picker);
        lang_dialog.show();
        lang_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RadioGroup radioGroup = lang_dialog.findViewById(R.id.xml_set_language_RG);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = lang_dialog.findViewById(checkedId);
                String lang = radioButton.getText().toString();

                if (lang.equals("English")) {

                    selected_language = "en";
                    lang_dialog.dismiss();
                    pref_editor.putString("LANGUAGE", selected_language);
                    pref_editor.commit();
                    set_language_tv.setText(lang);
                    change_language("en");

                    lang_dialog.dismiss();
                } else if (lang.equals("தமிழ்")) {

                    selected_language = "ta";
                    lang_dialog.dismiss();
                    pref_editor.putString("LANGUAGE", selected_language);
                    pref_editor.commit();

                    set_language_tv.setText(lang);
                    change_language("ta");

                }
            }
        });

    }

    private void change_language(String lang) {

        set_lang_locale = new Locale(lang);
        Locale.setDefault(set_lang_locale);
        Configuration configuration = new Configuration();
        configuration.locale = set_lang_locale;
        getActivity().getBaseContext().getResources().updateConfiguration(configuration, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //getActivity().recreate();

        try {
            getFragmentManager().beginTransaction()
                    .replace(R.id.xml_frame_layout, Setting_Fragment.class.newInstance()).commit();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }

    }


    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    private void set_notifcation() {

        String CHANNEL_ID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent intent = new Intent(getContext(), Splashscreen_Activity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

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
            notificationChannel.setDescription("Description");
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }


    private void currency_picker() {


        final Dialog curr_dialog = new Dialog(getContext());
        curr_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        curr_dialog.setContentView(R.layout.currency_picker);
        curr_dialog.show();
        curr_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        SearchView currency_search = curr_dialog.findViewById(R.id.xml_search_currency_edit);

        currency_RcView = curr_dialog.findViewById(R.id.xml_search_currency_list);
        currency_RcView.setHasFixedSize(true);
        currency_RcView.setLayoutManager(new LinearLayoutManager(getContext()));
        //------------------------------------------
        create_curr_array();
        //------------------------------------------

        curr_Adapter = new Currency_picker_Adapter(getContext(), currencyModels);
        currency_RcView.setAdapter(curr_Adapter);

        curr_Adapter.setonItemclicklisterner(new Currency_picker_Adapter.single_item_click_interface() {
            @Override
            public void onitemclick(String symbol, String curr_code, String curr_name) {
                String concat = symbol + " " + curr_code;
                edit_currency.setText(concat);

                // no save in setting page hence no need to update selected_curr_logo & selected_curr_code

                pref_editor.putString("CURRENCY_LOGO", symbol);
                pref_editor.putString("CURRENCY", curr_code);
                pref_editor.commit();
                curr_dialog.dismiss();
            }
        });

        currency_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchview_filterlist(newText);
                return true;
            }
        });
    }

    private void searchview_filterlist(String newText) {

        ArrayList<Currency_logo_model> filter_lists = new ArrayList<Currency_logo_model>();


        for (Currency_logo_model item : currencyModels) {
            if (item.getCurr_code().toLowerCase().contains(newText.toLowerCase())) {
                filter_lists.add(item);
            }
        }

        if (filter_lists.isEmpty()) {

            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();

        } else {
            curr_Adapter.filter_List(filter_lists);
        }

    }

    private void create_curr_array() {

        currencyModels = new ArrayList<>();
        currencyModels.add(new Currency_logo_model("€", "EUR", "Euro"));
        currencyModels.add(new Currency_logo_model("L", "ALL", "Albanian lek"));
        currencyModels.add(new Currency_logo_model("Br", "BYN", "Belarusian ruble"));
        currencyModels.add(new Currency_logo_model("KM", "BAM", "Bosnia and Herzegovina convertible mark"));
        currencyModels.add(new Currency_logo_model("лв", "BGN", "Bulgarian lev"));
        currencyModels.add(new Currency_logo_model("kn", "HRK", "Croatian kuna"));
        currencyModels.add(new Currency_logo_model("Kč", "CZK", "Czech koruna"));
        currencyModels.add(new Currency_logo_model("kr", "DKK", "Danish krone"));
        currencyModels.add(new Currency_logo_model("kn", "HRK", "Croatian Kuna"));
        currencyModels.add(new Currency_logo_model("₾", "GEL", "Georgian lari"));
        currencyModels.add(new Currency_logo_model("kr", "DKK", "Danish krone"));
        currencyModels.add(new Currency_logo_model("ft", "HUF", "Hungarian forint"));
        currencyModels.add(new Currency_logo_model("kr", "ISK", "Icelandic króna"));
        currencyModels.add(new Currency_logo_model("CHF", "CHF", "Swiss franc"));
        currencyModels.add(new Currency_logo_model("L", "MDL", "Moldovan leu"));
        currencyModels.add(new Currency_logo_model("ден", "MKD", "Second Macedonian denar"));
        currencyModels.add(new Currency_logo_model("kr", "NOK", "Norwegian krone"));
        currencyModels.add(new Currency_logo_model("zł", "PLN", "Polish zloty"));
        currencyModels.add(new Currency_logo_model("lei", "RON", "Romanian leu"));
        currencyModels.add(new Currency_logo_model("₽", "RUB", "Russian ruble"));
        currencyModels.add(new Currency_logo_model("RSD", "RSD", "Serbian dinar"));
        currencyModels.add(new Currency_logo_model("kr", "SEK", "Swedish krona"));
        currencyModels.add(new Currency_logo_model("CHF", "CHF", "Swiss franc"));
        currencyModels.add(new Currency_logo_model("₺", "TRY", "Turkish lira"));
        currencyModels.add(new Currency_logo_model("₴", "UAH", "Ukrainian hryvna"));
        currencyModels.add(new Currency_logo_model("£", "GBP", "Pounds sterling"));
        currencyModels.add(new Currency_logo_model("$", "USD", "United States Dollar"));
        currencyModels.add(new Currency_logo_model("$", "XCD", "East Caribbean dollar"));
        currencyModels.add(new Currency_logo_model("ƒ", "AWG", "Aruban florin"));
        currencyModels.add(new Currency_logo_model("$", "ARS", "Argentine peso"));
        currencyModels.add(new Currency_logo_model("B$", "BSD", "Bahamian dollar"));
        currencyModels.add(new Currency_logo_model("$", "BBD", "Barbadian dollar"));
        currencyModels.add(new Currency_logo_model("$", "BMD", "Bermudian dollar"));
        currencyModels.add(new Currency_logo_model("BZ$", "BZD", "Belize dollar"));
        currencyModels.add(new Currency_logo_model("Bs", "BOB", "Bolivian boliviano"));
        currencyModels.add(new Currency_logo_model("R$", "BRL", "Brazilian real"));
        currencyModels.add(new Currency_logo_model("CA$", "CAD", "Canadian dollar"));
        currencyModels.add(new Currency_logo_model("CI$", "KYD", "Cayman Islands dollar"));
        currencyModels.add(new Currency_logo_model("$", "CLP", "Chilean peso"));
        currencyModels.add(new Currency_logo_model("$", "COP", "Colombian peso"));
        currencyModels.add(new Currency_logo_model("₡", "CRC", "Costa Rican colón"));
        currencyModels.add(new Currency_logo_model("CUC$", "CUP", "Cuban peso"));
        currencyModels.add(new Currency_logo_model("ƒ", "ANG", "Netherlands Antillean guilder"));
        currencyModels.add(new Currency_logo_model("RD$", "DOP", "Dominican peso"));
        currencyModels.add(new Currency_logo_model("FK£", "FKP", "Falkland Islands pound"));
        currencyModels.add(new Currency_logo_model("Q", "GTQ", "Guatemalan quetzal"));
        currencyModels.add(new Currency_logo_model("G$", "GYD", "Guyanese dollar"));
        currencyModels.add(new Currency_logo_model("G", "HTG", "Haitian gourde"));
        currencyModels.add(new Currency_logo_model("L", "HNL", "Honduran lempira"));
        currencyModels.add(new Currency_logo_model("J$", "JMD", "Jamaican dollar"));
        currencyModels.add(new Currency_logo_model("$", "MXN", "Mexican peso"));
        currencyModels.add(new Currency_logo_model("C$", "NIO", "Nicaraguan córdoba"));
        currencyModels.add(new Currency_logo_model("B/.", "PAB", "Panamanian balboa"));
        currencyModels.add(new Currency_logo_model("₲", "PYG", "Paraguayan guaraní"));
        currencyModels.add(new Currency_logo_model("S/.", "PEN", "Peruvian sol"));
        currencyModels.add(new Currency_logo_model("ƒ", "ANG", "Netherlands Antillean guilder"));
        currencyModels.add(new Currency_logo_model("Sr$", "SRD", "Surinamese dollar"));
        currencyModels.add(new Currency_logo_model("TT$", "TTD", "Trinidad and Tobago dollar"));
        currencyModels.add(new Currency_logo_model("$U", "UYU", "Uruguayan peso"));
        currencyModels.add(new Currency_logo_model("Bs.", "VED", "Venezuelan bolívar"));
        currencyModels.add(new Currency_logo_model("؋", "AFN", "Afghan afghani"));
        currencyModels.add(new Currency_logo_model("֏, դր", "AMD", "Armenian dram"));
        currencyModels.add(new Currency_logo_model("₼", "AZN", "Azerbaijani manat"));
        currencyModels.add(new Currency_logo_model(".د.ب", "BHD", "Bahraini dinar"));
        currencyModels.add(new Currency_logo_model("€", "EUR", "Euro"));
        currencyModels.add(new Currency_logo_model("ლარი", "GEL", "Lari"));
        currencyModels.add(new Currency_logo_model("ع.د", "IQD", "Iraqi dinar"));
        currencyModels.add(new Currency_logo_model("﷼", "IRR", "Iranian rial"));
        currencyModels.add(new Currency_logo_model("₪", "ILS", "Israeli new shekel"));
        currencyModels.add(new Currency_logo_model("ينار", "JOD", "Jordanian dinar"));
        currencyModels.add(new Currency_logo_model("ك", "KWD", "Kuwaiti dinar"));
        currencyModels.add(new Currency_logo_model("ل.ل", "LBP", "Lebanese pound"));
        currencyModels.add(new Currency_logo_model("₪", "ILS", "Israeli shekel"));
        currencyModels.add(new Currency_logo_model("£S", "SYP", "Syrian pound"));
        currencyModels.add(new Currency_logo_model("AED", "AED", "Emirati dirham"));
        currencyModels.add(new Currency_logo_model("₪", "ILS", "Israeli shekel"));
        currencyModels.add(new Currency_logo_model("ر.ع", "OMR", "Omani rial"));
        currencyModels.add(new Currency_logo_model("ر.ق", "QAR", "Qatari riyal"));
        currencyModels.add(new Currency_logo_model("SR", "SAR", "Saudi riyal"));
        currencyModels.add(new Currency_logo_model("﷼", "YER", "Yemeni rial"));
        currencyModels.add(new Currency_logo_model("FCFA", "XAF", "CFA franc"));
        currencyModels.add(new Currency_logo_model("CFA", "XOF", "CFA franc"));
        currencyModels.add(new Currency_logo_model("دج", "DZD", "Algerian dinar"));
        currencyModels.add(new Currency_logo_model("Kz", "AOA", "Angolan kwanza"));
        currencyModels.add(new Currency_logo_model("P", "BWP", "Botswana pula"));
        currencyModels.add(new Currency_logo_model("FBu", "BIF", "Burundian franc"));
        currencyModels.add(new Currency_logo_model("CVE", "CVE", "Cape Verdean escudo"));
        currencyModels.add(new Currency_logo_model("CF", "KMF", "Comorian franc"));
        currencyModels.add(new Currency_logo_model("FC", "CDF", "Congolese franc"));
        currencyModels.add(new Currency_logo_model("Fdj", "DJF", "Djiboutian franc"));
        currencyModels.add(new Currency_logo_model("E£", "EGP", "Egyptian pound"));
        currencyModels.add(new Currency_logo_model("Nkf", "ERN", "Eritrean nakfa"));
        currencyModels.add(new Currency_logo_model("Br", "ETB", "Ethiopian birr"));
        currencyModels.add(new Currency_logo_model("L", "SZL", "Lilangeni"));
        currencyModels.add(new Currency_logo_model("D", "GMD", "Dalasi"));
        currencyModels.add(new Currency_logo_model("GH₵", "GHS", "Ghanaian cedi"));
        currencyModels.add(new Currency_logo_model("FG", "GNF", "Guinean franc"));
        currencyModels.add(new Currency_logo_model("KSh", "KES", "Kenyan shilling"));
        currencyModels.add(new Currency_logo_model("L", "LSL", "Lesotho loti"));
        currencyModels.add(new Currency_logo_model("LD$", "LRD", "Liberian dollar"));
        currencyModels.add(new Currency_logo_model("LD", "LYD", "Libyan dinar"));
        currencyModels.add(new Currency_logo_model("Ar", "MGA", "Malagasy ariary"));
        currencyModels.add(new Currency_logo_model("K", "MWK", "Malawian kwacha"));
        currencyModels.add(new Currency_logo_model("₨", "MUR", "Mauritian rupee"));
        currencyModels.add(new Currency_logo_model("UM", "MRU", "Ouguiya"));
        currencyModels.add(new Currency_logo_model("DH", "MAD", "Moroccan dirham"));
        currencyModels.add(new Currency_logo_model("MT", "MZN", "Mozambican metical"));
        currencyModels.add(new Currency_logo_model("N$", "NAD", "Namibian dollar"));
        currencyModels.add(new Currency_logo_model("₦", "NGN", "Nigerian naira"));
        currencyModels.add(new Currency_logo_model("R₣", "RWF", "Rwandan franc"));
        currencyModels.add(new Currency_logo_model("Db", "STN", "São Tomé and Príncipe dobra"));
        currencyModels.add(new Currency_logo_model("SR", "SCR", "Seychellois rupee"));
        currencyModels.add(new Currency_logo_model("Le", "SLL", "Sierra Leonean leone"));
        currencyModels.add(new Currency_logo_model("Sh.So.", "SOS", "Somali shilling"));
        currencyModels.add(new Currency_logo_model("R", "ZAR", "South african rand"));
        currencyModels.add(new Currency_logo_model("SS£", "SSP", "South Sudanese pound"));
        currencyModels.add(new Currency_logo_model("SDG", "SDG", "Sudanese pound"));
        currencyModels.add(new Currency_logo_model("TSh", "TZS", "Tanzanian shilling"));
        currencyModels.add(new Currency_logo_model("د.ت", "TND", "Tunisian dinar"));
        currencyModels.add(new Currency_logo_model("USh", "UGX", "Ugandan shilling"));
        currencyModels.add(new Currency_logo_model("$", "USD", "United States Dollar"));
        currencyModels.add(new Currency_logo_model("A$", "AUD", "Australian dollar"));
        currencyModels.add(new Currency_logo_model("৳", "BDT", "Bangladeshi taka"));
        currencyModels.add(new Currency_logo_model("Nu", "BTN", "Bhutanese ngultrum"));
        currencyModels.add(new Currency_logo_model("B$", "BND", "Brunei dollar"));
        currencyModels.add(new Currency_logo_model("៛", "KHR", "Cambodian riel"));
        currencyModels.add(new Currency_logo_model("¥", "CNY", "Chinese yuan"));
        currencyModels.add(new Currency_logo_model("HK$", "HKD", "Hong Kong dollar"));
        currencyModels.add(new Currency_logo_model("Rp", "IDR", "Indonesian rupiah"));
        currencyModels.add(new Currency_logo_model("₹", "INR", "Indian rupee"));
        currencyModels.add(new Currency_logo_model("¥", "JPY", "Japanese yen"));
        currencyModels.add(new Currency_logo_model("₸", "KZT", "Kazakhstani tenge"));
        currencyModels.add(new Currency_logo_model("som", "KGS", "Kyrgyzstani som"));
        currencyModels.add(new Currency_logo_model("₭", "LAK", "Lao kip"));
        currencyModels.add(new Currency_logo_model("MOP$", "MOP", "Macanese pataca"));
        currencyModels.add(new Currency_logo_model("RM", "MYR", "Malaysian ringgit"));
        currencyModels.add(new Currency_logo_model("MRf", "MVR", "Maldivian rufiyaa"));
        currencyModels.add(new Currency_logo_model("₮", "MNT", "Mongolian tögrög"));
        currencyModels.add(new Currency_logo_model("K", "MMK", "Kyat"));
        currencyModels.add(new Currency_logo_model("Rs", "NPR", "Nepalese rupee"));
        currencyModels.add(new Currency_logo_model("$", "NZD", "New Zealand dollar"));
        currencyModels.add(new Currency_logo_model("₩", "KPW", "North Korean won"));
        currencyModels.add(new Currency_logo_model("Rs", "PKR", "Pakistani rupee"));
        currencyModels.add(new Currency_logo_model("₱", "PHP", "Philippine peso"));
        currencyModels.add(new Currency_logo_model("S$", "SGD", "Singapore dollar"));
        currencyModels.add(new Currency_logo_model("₩", "KRW", "South Korean won"));
        currencyModels.add(new Currency_logo_model("Rs", "LKR", "Sri Lankan rupee"));
        currencyModels.add(new Currency_logo_model("NT$", "TWD", "New Taiwan dollar"));
        currencyModels.add(new Currency_logo_model("TJS", "TJS", "Somoni"));
        currencyModels.add(new Currency_logo_model("US$", "USD", "US Dollar"));
        currencyModels.add(new Currency_logo_model("฿", "THB", "Thai baht"));
        currencyModels.add(new Currency_logo_model("m", "TMT", "Turkmen new manat"));
        currencyModels.add(new Currency_logo_model("som", "UZS", "Uzbekistan som"));
        currencyModels.add(new Currency_logo_model("₫", "VND", "Vietnamese đồng"));

    }


}