package com.kalingantech.neverduedev.DB;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class Subs_ViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public Subs_ViewModelFactory(Application myApplication) {
        application = myApplication;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new Subs_ViewModel(application);
    }
}
