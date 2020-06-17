package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class Workout implements Parcelable {
    // Fields
    private String name ="";
    private String uniqueID; // Generated in database?
    private int time;

    private ArrayList<Exercise> exercises = new ArrayList<>();
    private String workoutType;

    private ArrayList<Boolean> recurring = new ArrayList<>();
    private boolean notifications, publicWorkout;


    public Workout(){ }

    public Workout(String name){
        this.name = name;
        workoutType = WorkoutType.DEFAULT.name();
        for(int i = 0; i<7;i++) {
            recurring.set(i,false);
        }
    }

    public Workout(String name, String type){
        this(name);
        workoutType = type;
    }

    public Workout(String name, ArrayList<Exercise> exercises){
        this(name);
        this.exercises=exercises;
        workoutType = WorkoutType.DEFAULT.name();

        Log.i("Workout/Constructor", "Created a workout with an arraylist");
    }

    protected Workout(Parcel in) {
        name = in.readString();
        uniqueID = in.readString();
        time = in.readInt();
        exercises = in.readArrayList(Workout.class.getClassLoader());

        // Boolean settings
        in.readArrayList(recurring.getClass().getClassLoader());
        int notiInt = in.readInt();
        notifications = (notiInt == 1);
        int pubInt = in.readInt();
        publicWorkout = (pubInt == 1);
        Log.i("Workout/Constructor", "Created a workout with parcel");
    }


    public void addExercise (Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public String getName() {
        return name;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getWorkoutType() {
        return this.workoutType;
    }

    public int getTime() {
        int time = 0;
        for(Exercise x : exercises)
        {
            time += x.getTime();
        }
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String TimeAsString() {
        int time = getTime();
        int minutes = time / 60;
        int seconds = time % 60;
        return seconds < 10 ? minutes + ":" + "0" + seconds : minutes + ":" + seconds;
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(uniqueID);
        parcel.writeInt(time);
        parcel.writeTypedList(exercises);
        parcel.writeList(recurring);
        parcel.writeInt(notifications ? 1 : 0);
        parcel.writeInt(publicWorkout ? 1 : 0);

    }



    public boolean isPublicWorkout() {
        return publicWorkout;
    }

    public void setPublicWorkout(boolean publicWorkout) {
        this.publicWorkout = publicWorkout;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public ArrayList<Boolean> getRecurring() {
        return recurring;
    }

    public void setRecurring(ArrayList<Boolean> recurring) {
        this.recurring = recurring;
    }

    public void setName(String toString) {
        name = toString;
    }
}
