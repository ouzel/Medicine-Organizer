package com.example.myapplication.calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;

public class ReminderBroadcast extends BroadcastReceiver {

    public static String notifName = "new medicine";

    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notiff")
                .setSmallIcon(R.drawable.ic_baseline_calendar_month_24)
                .setContentTitle("Medicine Organizer")
                .setContentText(notifName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());

    }
}