package com.example.youfit.domain;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    // Fields
    private String name;
    private String uniqueID; // Generated in database?

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
}
