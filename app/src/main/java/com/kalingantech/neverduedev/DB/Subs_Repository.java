package com.kalingantech.neverduedev.DB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Subs_Repository {

    private Subs_DAO msubs_DAO;
/*
    private LiveData<List<Subs_list>> msubs_live_list;
    private LiveData<List<Subs_list>> msearch_list;
    private LiveData<List<Subs_list>> list_by_uid;
*/

    public Subs_Repository(Application application) {
        Subs_RoomDatabase subs_roomDatabase = Subs_RoomDatabase.getDatabase(application);
        msubs_DAO = subs_roomDatabase.subs_DAO();
    }

    public void insertSubs_rep(Subs_list subs_list) {
        Subs_RoomDatabase.databaseWriteExecutor.execute(() -> msubs_DAO.insert(subs_list));
    }

    public void update_sub_rep(Subs_list update_subs_list) {
                Subs_RoomDatabase.databaseWriteExecutor.execute(() -> msubs_DAO.update(update_subs_list));
    }

    public void delete_sub_rep(Subs_list delete_subs_list) {
        Subs_RoomDatabase.databaseWriteExecutor.execute(() -> msubs_DAO.delete_sub(delete_subs_list));
    }

    public LiveData<List<Subs_list>> getAllSubs_list_by_name()  {
        return msubs_DAO.getSubs_list_by_name();
    }

    public LiveData<List<Subs_list>> getAllSubs_list_by_price()  {
        return msubs_DAO.getSubs_list_by_price();
    }

    public LiveData<List<Subs_list>> getAllSubs_list_by_date()  {
        return msubs_DAO.getSubs_list_by_date();
    }

    public LiveData<List<Subs_list>> getsearch_list(String search) {
        return msubs_DAO.getSubs_search_list(search);
    }

    public LiveData<Subs_list> getsearch_by_uid(int uid) {
        return msubs_DAO.getsub_by_uid(uid);
    }

}
