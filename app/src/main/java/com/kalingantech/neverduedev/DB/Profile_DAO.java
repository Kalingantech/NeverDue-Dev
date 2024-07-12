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
public interface Profile_DAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Profile_list profile_list);

    @Update
    void update(Profile_list profile_list);

    @Delete
    void delete_sub(Profile_list profile_list);

    @Query("SELECT * from profile_table ORDER By profile_name Asc")
    LiveData<List<Profile_list>> get_all_profile_names();

}
