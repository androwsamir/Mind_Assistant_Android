package com.example.mindassistant;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

        //String tableName = intent.getExtras().getString("tablename");
        //String activityName = intent.getExtras().getString("activityName");

        //System.out.println(tableName);
        //System.out.println(activityName);

        Intent i = new Intent(context, MainActivity.class);
        //i.putExtra("tablename", tableName);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, 0);
        nb.setContentIntent(pendingIntent);
        notificationHelper.getManager().notify(1, nb.build());
    }
}