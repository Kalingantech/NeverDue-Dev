package com.kalingantech.neverduedev.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Subs_list.class}, version = 1, exportSchema = false)
public abstract class Subs_RoomDatabase extends RoomDatabase {
    public abstract Subs_DAO subs_DAO();

    private static volatile Subs_RoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static Subs_RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Subs_RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    Subs_RoomDatabase.class, "subs_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}