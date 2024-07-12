package com.kalingantech.neverduedev.Utils;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.kalingantech.neverduedev.DB.Subs_Repository;
import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.DB.Subs_list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Reboot_Receiver extends BroadcastReceiver {

    List<Subs_list> list = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {


        //if the recieve toye is reboot, get the sublist and rebuild the alarm.
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            //since we dont need to observer the data, getting directly from repository. And since we cannot get the data on main thread creating new thread.
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Subs_Repository repo = new Subs_Repository((Application) context.getApplicationContext());
                    list = repo.getAllSubs_list();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("PREF", 0);
                    SharedPreferences.Editor pref_editor = sharedPreferences.edit();
                    long time = sharedPreferences.getLong("ALARM", Calendar.getInstance().getTimeInMillis()); //"Calendar.getInstance().getTimeInMillis()" this is deault


                    schedule(context,list,time);

                }
            });
            t1.start();






            /*// reset your alarms ere


            //=================================================



*/

            }
            //=================================================
        }

        private void schedule(Context context, List<Subs_list> list,long time){

            for(Subs_list item : list){
                Log.d("AFTER_REBOOT_inside", item.getName());

                //create intent and attach it ot builder
                Intent intent_new = new Intent(context.getApplicationContext(), Notification_new.class);
                intent_new.putExtra("NOTIFICATION_CODE",item.getNotification_code());
                intent_new.putExtra("NOTIFICATION_NAME",item.getName());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), item.getNotification_code(), intent_new, PendingIntent.FLAG_IMMUTABLE |  PendingIntent.FLAG_UPDATE_CURRENT );

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,time , pendingIntent);

            }
        }

    }



