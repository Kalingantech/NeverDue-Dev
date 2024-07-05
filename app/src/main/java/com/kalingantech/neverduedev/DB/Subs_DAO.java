package com.kalingantech.neverduedev.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Subs_DAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Subs_list subs_list);

    @Update
    void update(Subs_list subs_list);

    @Delete
    void delete_sub(Subs_list subs_list);


    //delete all
    @Query("DELETE from subs_table")
    void deleteAll();


    @Query("SELECT * from subs_table ORDER By name Asc")
    LiveData<List<Subs_list>> getSubs_list_by_name();

    @Query("SELECT * from subs_table ORDER By price Desc")
    LiveData<List<Subs_list>> getSubs_list_by_price();

    @Query("SELECT * from subs_table ORDER By billing_next_date Asc")
    LiveData<List<Subs_list>> getSubs_list_by_date();


    @Query("SELECT * from subs_table WHERE name LIKE '%'||:search||'%'")
    LiveData<List<Subs_list>> getSubs_search_list(String search);

    @Query("SELECT * from subs_table WHERE uid LIKE :uid")
    LiveData<Subs_list> getsub_by_uid(int uid);


}
