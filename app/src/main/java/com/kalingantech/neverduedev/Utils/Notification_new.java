package com.kalingantech.neverduedev.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.kalingantech.neverduedev.DB.Subs_ViewModel;
import com.kalingantech.neverduedev.R;

public class Notification_new extends BroadcastReceiver {

    //public static int NOTIFICATION_ID = 42;
    public static String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

        String temp_name = intent.getStringExtra("NOTIFICATION_NAME");
        int temp_code = intent.getIntExtra("NOTIFICATION_CODE",1);

        //setting the Notify builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Never Due")
                .setContentText(temp_name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //finally start the notification
        notificationManager.notify(temp_code, builder.build()); //1 is just default value
    }
}
