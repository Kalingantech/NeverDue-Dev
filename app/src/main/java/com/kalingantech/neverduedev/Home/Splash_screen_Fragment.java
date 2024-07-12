package com.kalingantech.neverduedev.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kalingantech.neverduedev.R;


public class Splash_screen_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        Intent loginintent = new Intent(getContext(), MainActivity.class);
        startActivity(loginintent);

        return view;
    }
}