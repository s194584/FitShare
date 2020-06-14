package com.example.youfit.domain;

import java.util.List;

public class Workout {
    // Fields
    private String name;
    private String uniqueID; // Generated in database?

    private List<Exercise> exercises;
    private WorkoutType workoutType;

    public Workout(String name){
        this.name = name;
        workoutType = WorkoutType.DEFAULT;
    }

    public Workout(String name, WorkoutType type){
        this(name);
        workoutType = type;
    }

    public Workout(String name, List<Exercise> exercises){
        this(name);
        this.exercises=exercises;
        workoutType = WorkoutType.DEFAULT;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
