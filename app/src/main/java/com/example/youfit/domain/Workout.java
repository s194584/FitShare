package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Workout implements Parcelable {
    // Fields
    private String name;
    private String uniqueID; // Generated in database?
    private String time = null;

    private ArrayList<Exercise> exercises = new ArrayList<>();;
    private WorkoutType workoutType;

    private boolean[] recurring = new boolean[7];
    private boolean notifications, publicWorkout;

    public Workout(){
        name = "";
    }

    public Workout(String name){
        this.name = name;
        workoutType = WorkoutType.DEFAULT;
    }

    public Workout(String name, WorkoutType type){
        this(name);
        workoutType = type;
    }

    public Workout(String name, ArrayList<Exercise> exercises){
        this(name);
        this.exercises=exercises;
        workoutType = WorkoutType.DEFAULT;

        Log.i("Workout/Constructor", "Created a workout with an arraylist");
    }

    protected Workout(Parcel in) {
        name = in.readString();
        uniqueID = in.readString();
        time = in.readString();
        exercises = in.readArrayList(Workout.class.getClassLoader());

        // Boolean settings
        in.readBooleanArray(recurring);
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

    public int getTime() {
        int time = 0;
        for(Exercise x : exercises)
        {
            time += x.getTime();
        }
        return time;
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
        parcel.writeString(time);
        parcel.writeTypedList(exercises);
        parcel.writeBooleanArray(recurring);
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

    public boolean[] getRecurring() {
        return recurring;
    }

    public void setRecurring(boolean[] recurring) {
        this.recurring = recurring;
    }

    public void setName(String toString) {
        name = toString;
    }
}
