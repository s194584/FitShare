package com.example.youfit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int[] recurring = intent.getIntArrayExtra("recurring");
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2;
        // Sunday = 1 is corrected to 6
        if(currentDay==-1){
            currentDay = 6;
        }
        // Open app intent
        Intent openAppIntent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,openAppIntent,0);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String message = "We can see you have "+recurring[currentDay]+" workout(s)! Let's get going!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"FORNOTI");
        builder.setContentText(message)
                .setContentTitle(context.getString(R.string.notification_title))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);
        Log.i(TAG,"Sending notification");

        notificationManager.notify(200,builder.build());
    }
}
