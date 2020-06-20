package com.example.youfit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AlarmReceiver","Alarm received!!!");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"FORNOTI");
        builder.setContentText("Hey, got any workouts?")
                .setContentTitle("Your favorite workout app")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setAutoCancel(true);
        Log.i(TAG,"Sending notification");

        notificationManager.notify(200,builder.build());
    }
}
