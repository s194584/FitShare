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
//        int[] recurring = intent.getIntArrayExtra("recurring");
        int[] recurring = new int[7];
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2; //TODO Den burde nok sættes automatisk
        // Sunday = 1 is corrected to 6
        if(currentDay==-1){
            currentDay = 6;
        }
        Log.i(TAG, "onReceive: "+recurring.toString());

        // Open app intent
        Intent openAppIntent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,openAppIntent,0);

        Log.i("AlarmReceiver","Alarm received!!!");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"FORNOTI");
        builder.setContentText("Hey, got any workouts? We can see you have "+recurring[currentDay]+" workouts!")
                .setContentTitle("Your favorite workout app")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Log.i(TAG,"Sending notification");

        notificationManager.notify(200,builder.build());
    }
}
