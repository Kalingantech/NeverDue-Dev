package com.kalingantech.neverduedev.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "profile_table")
public class Profile_list {

    @PrimaryKey()
    @NonNull
    public String profile_name;

    public Profile_list(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }
}
