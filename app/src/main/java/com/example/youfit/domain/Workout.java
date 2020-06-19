package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Workout implements Parcelable {

    private String name ="";
    private String uniqueID;
    private long time;
    private String description;

    private ArrayList<Exercise> exercises = new ArrayList<>();
    private String workoutType;
    private String workoutDifficulty;

    private ArrayList<Boolean> recurring = new ArrayList<>();
    private boolean notifications, publicWorkout;

    public Workout(){ } //Empty constructor for firebase

    public Workout(String name){
        this.name = name;
        workoutType = WorkoutType.DEFAULT.name();
        workoutDifficulty = WorkoutDifficulty.UNDEFINED.name();
        description = "This is a workout description";
        for(int i = 0; i<7;i++) {
            recurring.add(i,false);
        }
        Log.i("Workout/Constructor", "Created a workout from name");
    }

    public Workout(String name, String type){
        this(name);
        workoutType = type;
        workoutDifficulty = WorkoutDifficulty.UNDEFINED.name();
        description = "This is a workout description";

        Log.i("Workout/Constructor", "Created a workout from name and type");
    }

    public Workout(String name, ArrayList<Exercise> exercises){
        this(name);
        this.exercises=exercises;
        workoutType = WorkoutType.DEFAULT.name();
        workoutDifficulty = WorkoutDifficulty.UNDEFINED.name();
        description = "This is a workout description";

        Log.i("Workout/Constructor", "Created a workout from name and arraylist");
    }

    protected Workout(Parcel in) {
        name = in.readString();
        uniqueID = in.readString();
        time = in.readLong();
        exercises = in.createTypedArrayList(Exercise.CREATOR);

        // Boolean settings
        in.readArrayList(recurring.getClass().getClassLoader());
        int notiInt = in.readInt();
        notifications = (notiInt == 1);
        int pubInt = in.readInt();
        publicWorkout = (pubInt == 1);
        Log.i("Workout/Constructor", "Created a workout from a parcel");
    }

    public Workout(Workout workout) {
        this.name = workout.name;
        this.time = workout.time;
        this.description = workout.description;
        this.workoutType = workout.workoutType;
        this.workoutDifficulty = workout.workoutDifficulty;
        this.recurring = workout.recurring;
        this.notifications = workout.notifications;
        this.publicWorkout = workout.publicWorkout;

        ArrayList<Exercise> tempExercises = new ArrayList<>();
        for (Exercise e: workout.exercises) {
            tempExercises.add(new Exercise(e));
        }
        this.exercises = tempExercises;

        Log.i("Workout/Constructor", "Created a workout from a workout");
    }

    public String getUniqueID() {
        return uniqueID;
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

    public String getDescription() {
        return this.description;
    }

    public String getWorkoutType() {
        return this.workoutType;
    }

    public String getWorkoutDifficulty() {
        return this.workoutDifficulty;
    }


    public boolean getPublic() { return this.publicWorkout; }

    public String formatType() {
        String type = getWorkoutType();
        if (type != null) {
            String firstLetter = type.substring(0,1);
            String rest = type.substring(1);
            return firstLetter.toUpperCase() + rest.toLowerCase();
        }
        return null;
    }

    public String formatDifficulty() {
        String difficulty = getWorkoutDifficulty();
        if (difficulty != null) {
            String firstLetter = difficulty.substring(0,1);
            String rest = difficulty.substring(1);
            return firstLetter.toUpperCase() + rest.toLowerCase();
        }
        return null;
    }

    public long getTime() {
        long time = 0;
        for(Exercise x : exercises)
        {
            time += Integer.parseInt(x.getAmountString());
        }
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String TimeAsString() {
        long time = getTime()/1000;
        long minutes = time / 60;
        long seconds = time % 60;
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
        parcel.writeLong(time);
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
