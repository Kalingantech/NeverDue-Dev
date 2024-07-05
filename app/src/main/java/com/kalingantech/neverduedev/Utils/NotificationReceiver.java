package com.kalingantech.neverduedev.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.kalingantech.neverduedev.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static int NOTIFICATION_ID = 42;
    public static String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        //setting the Notify builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //finally start the notification
        notificationManager.notify(NOTIFICATION_ID,builder.build());

    }
}
