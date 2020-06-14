package com.example.youfit.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    // Fields
    private String name;
    private String uniqueID; // Generated in database?
    private String time = null;

    private ArrayList<Exercise> exercises = new ArrayList<>();;
    private WorkoutType workoutType;

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
}
