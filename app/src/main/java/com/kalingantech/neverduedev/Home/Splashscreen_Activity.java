package com.kalingantech.neverduedev.Home;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.kalingantech.neverduedev.R;

import java.util.Locale;

public class Splashscreen_Activity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor pref_editor;
    int Login_times;

    public NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(Splashscreen_Activity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Splashscreen_Activity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);

            }

        } else {
            Toast.makeText(this, "my device", Toast.LENGTH_SHORT).show();
        }
*/
        sharedPreferences = this.getSharedPreferences("PREF", 0);
        pref_editor = sharedPreferences.edit();
        Login_times = sharedPreferences.getInt("LOGIN", 0);

        String selected_language = sharedPreferences.getString("LANGUAGE", "en");
        load_language(selected_language);

        getSupportFragmentManager().beginTransaction().replace(R.id.xml_splash_screenlayout, new Onboarding_Fragment()).commit();
        //checklogins(Login_times);
    }

    private void load_language(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }


    private void checklogins(int Login_count) {

        getSupportFragmentManager().beginTransaction().replace(R.id.xml_splash_screenlayout, new Onboarding_Fragment()).commit();
       /* if (Login_count == 0) {
            //first time login
            pref_editor.putInt("LOGIN", 1);
            pref_editor.commit();
            //first time login after installation
            getSupportFragmentManager().beginTransaction().replace(R.id.xml_splash_screenlayout, new Onboarding_Fragment()).commit();
        } else {
            //after first time login
            getSupportFragmentManager().beginTransaction().replace(R.id.xml_splash_screenlayout, new Splash_screen_Fragment()).commit();
            checkconnection();
        }*/
    }


    private void checkconnection() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginintent = new Intent(Splashscreen_Activity.this, MainActivity.class);
                loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginintent);
                finish();

            }
        }, 1000);
    }

}

/*-----------------
 * android.nonFinalResIds=false - added this flag in gradle properties when using "R.id." options not working
 *android:allowBackup="true"  --
 * */