package com.example.youfit;

import com.example.youfit.R.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import com.example.youfit.domain.Statistics;
import com.example.youfit.domain.Workout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SignOutDialogListener, ChangePasswordDialogListener, Server.OnServerSetupCompleteListener, DatabaseListener {

    private static final int RESULT_CODE_DOWORKOUT = 214;
    protected String TAG = "MainActivity";

    protected boolean alreadyLoggedIn = false;
    protected Server server;
    protected ExerciseElementList exerciseElementList = new ExerciseElementList();
    protected boolean notifications;
    private ArrayList<Workout> workouts = new ArrayList<>();
    private Statistics stats;
    protected ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate ");
        super.onCreate(savedInstanceState);
        this.server = new Server(this);
        createNotificationChannel();

        server.loadUserNotifications(this);
        server.loadCurrentUsersWorkouts(this);
        server.loadUserStats(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cancelNotifications();
    }

    @Override
    public void onComplete(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getKey().equals("notifications")){
            notifications = Boolean.parseBoolean(dataSnapshot.getValue().toString());
        } else if(dataSnapshot.getKey().equals("statistics")){
            stats = dataSnapshot.getValue(Statistics.class);
        }else {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                workouts.add(data.getValue(Workout.class));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setupNotifications(notifications,workouts);
        Log.i(TAG, "onStop: Trying to go for notifications");
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

    public boolean isNotifications() {
        return notifications;
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
        if (exerciseElementList.getElementHashMap().isEmpty()) {
            setContentView(layout.activity_main);
            setUpNavigation();
            exerciseElementList.setElementHashMap(server.getPreDefinedExercises());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: in MainActivity");
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE_DOWORKOUT){
            Log.i(TAG, "onActivityResult: GotResult!");
            stats.addTotalWorkoutsTotal(1);
            stats.addTotalTimeTotal(data.getExtras().getLong("timeSpent"));
            server.changeStats(stats);
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel for API level 26 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Channel settings
            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
            channel.setDescription(description);
            // Creating channel
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void cancelNotifications(){
        setupNotifications(false,null);
    }

    private void setupNotifications(boolean b, ArrayList<Workout> workouts){

        //Morning alarm!
        // Setting the time for trigger
//        Calendar calendar = Calendar.getInstance();
//        long currentTimeInMillis = calendar.getTimeInMillis();
//        calendar.set(Calendar.HOUR_OF_DAY,8);
//        long timeInMillis = calendar.getTimeInMillis();

        Calendar calender = Calendar.getInstance();
        long timeInMillis = 1000*60*15;
        // If it is already past (eight) o'clock
//        if (timeInMillis < currentTimeInMillis){
//            timeInMillis += 1000*60*60*24;
//        }

        // Creating intent for AlarmReceiver
        Intent notificationIntent = new Intent(this,AlarmReceiver.class);
        notificationIntent.setAction(getString(R.string.ALARM_ACTION));

        // Adding extras based on weekly workouts
        if(b){
            notificationIntent.putExtra("recurring", getNumberOfDailyWorkouts(workouts));
        }

        // Creating the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);

        // Setting or cancelling the alarm and pending intent
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


        if(b){
            Log.i(TAG, "setNotificationAlarm: setting alarm");
             alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent); // Real alarm
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+6*1000,5*1000, pendingIntent); // Test alarm
        }else
            {
                pendingIntent.cancel();
            }
    }

    private int[] getNumberOfDailyWorkouts(ArrayList<Workout> workouts){
        int[] daily = new int[7];
        for (Workout workout:workouts) {
            ArrayList<Boolean> recurring = workout.getRecurring();
            for (int i = 0; i<7; i++){
                if(recurring.get(i)){
                    daily[i] += 1;
                }
            }
        }
        Log.i(TAG, "getNumberOfDailyWorkouts: "+daily[0]+", "+daily[1]+", "+daily[2]+", "+daily[3]+", "+daily[4]+", "+daily[5]+", "+daily[6]);
        return daily;
    }
}