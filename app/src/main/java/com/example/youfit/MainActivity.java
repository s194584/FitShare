package com.example.youfit;

import com.example.youfit.R.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.ExerciseElement;
import com.example.youfit.domain.ExerciseElementList;
import com.example.youfit.domain.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SignOutDialogListener, ChangePasswordDialogListener, Server.OnServerSetupCompleteListener, DatabaseListener {

    protected String TAG = "Server";

    protected boolean alreadyLoggedIn = false;
    protected Server server;
    protected ExerciseElementList exerciseElementList = new ExerciseElementList();
    protected boolean notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.server = new Server(this);
        createNotificationChannel();
        server.loadUserNotifications(this);
        //TODO: Make waiting screen for database call back.

    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNotifications(false);
    }

    @Override
    public void onComplete(DataSnapshot dataSnapshot) {
        notifications = (boolean) dataSnapshot.getValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setupNotifications(notifications);
        server.changeNotifications(notifications);
    }

    public void setUpNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof ChangePasswordDialogFragment){

            // Uses the Firebase build in function .sendPasswordResetEmail to reset users password.
            FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Password reset email sent!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        if (dialog instanceof SignOutDialogFragment) {
            FirebaseAuth.getInstance().signOut(); //log out
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public Server getServer() {
        return this.server;
    }

    public ExerciseElementList getHashMap()
    {
        return this.exerciseElementList;
    }

    public void addPreDefinedExercise(ExerciseElement exerciseElement){
        exerciseElementList.addElement(exerciseElement);
        server.addPreDefinedExercise(exerciseElement,exerciseElement.getName());
    }

    @Override
    public void onSetupComplete() {
        Log.i(TAG,"Setting up views.");
        setContentView(layout.activity_main);
        setUpNavigation();
        exerciseElementList.setHashMap(server.getPreDefinedExercises());
//        notifications = server.getCurrentUser().getNotifications();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupNotifications(boolean b){
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY,8);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,8);
        long timeInMillis = calendar.getTimeInMillis();

        if (timeInMillis < currentTimeInMillis){
            // Add a day
            timeInMillis += 1000*60*60*24;
        }

        Intent notificationIntent = new Intent(this,AlarmReceiver.class);
        notificationIntent.putExtra("recurring", new boolean[7]);
        notificationIntent.setAction(getString(R.string.ALARM_ACTION));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i(TAG, "setNotificationAlarm: setting an alarm");
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if(b){
            // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent); // Real alarm
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+6*1000,5*1000, pendingIntent); // Test alarm
            notificationIntent.putExtra("recurring", new int[7]);
        }else{
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}