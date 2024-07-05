package com.kalingantech.neverduedev.Home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kalingantech.neverduedev.R;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.xml_frame_layout, new Home_Fragment()).commit();

    }

    @Override
    public void onBackPressed() {
        
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            getSupportFragmentManager().popBackStack();
            //super.onBackPressed();
            //additional code
        } else {
            super.onBackPressed();
            //getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.xml_frame_layout, new Home_Fragment()).commit();

        }

    }

}
