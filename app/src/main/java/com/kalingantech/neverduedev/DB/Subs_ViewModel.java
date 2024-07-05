package com.kalingantech.neverduedev.DB;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Subs_ViewModel extends AndroidViewModel {



    private Subs_Repository subs_repository;
    private LiveData<List<Subs_list>> list_LiveData_by_name;
    private LiveData<List<Subs_list>> list_LiveData_by_price;

    private LiveData<List<Subs_list>> list_LiveData_by_date;

    private LiveData<List<Subs_list>> search_list_by_uid;
    public MutableLiveData<List<Subs_list>> list_LiveData_mutable;

    public Subs_ViewModel(Application application) {
        super(application);
        subs_repository = new Subs_Repository(application);
        list_LiveData_by_name = subs_repository.getAllSubs_list_by_name();
        list_LiveData_by_price = subs_repository.getAllSubs_list_by_price();
        list_LiveData_by_date = subs_repository.getAllSubs_list_by_date();

        //search_list_LiveData = subs_repository.getsearch_list();
    }

    public LiveData<List<Subs_list>> getAllSubsFromVm_by_name() {
        return list_LiveData_by_name;
    }
    public LiveData<List<Subs_list>> getAllSubs_list_by_price() {
        return list_LiveData_by_price;
    }

    public LiveData<List<Subs_list>> getAllSubs_list_by_date() {
        return list_LiveData_by_date;
    }
    public MutableLiveData<List<Subs_list>> getAllSubsFromVm_mutable() {
        return list_LiveData_mutable;
    }
    public LiveData<List<Subs_list>> getsearchFromVm(String search) {
        return subs_repository.getsearch_list(search);
    }

    public LiveData<Subs_list> getSearch_list_by_uid(int uid) {
        return subs_repository.getsearch_by_uid(uid);
    }


    public void insertsubs_list(Subs_list subs_list) {
        subs_repository.insertSubs_rep(subs_list);
    }

    public void update_sub_rep(Subs_list update_subs_list) {
        subs_repository.update_sub_rep(update_subs_list);
    }

    public void delete_sub_rep(Subs_list update_subs_list) {
        subs_repository.delete_sub_rep(update_subs_list);
    }

}