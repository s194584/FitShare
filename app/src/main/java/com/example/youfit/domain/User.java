package com.example.youfit.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;

    private List<Workout> savedWorkouts = new ArrayList<>();

    public User() {} //Empty constructor for firebase

    public User(String name) {
        this.name = name;
        Log.i("User/constructor","Created user from name");
    }

    public User(String name, List<Workout> savedWorkouts) {
        this.name = name;
        this.savedWorkouts = savedWorkouts;
        Log.i("User/constructor","Created user from name and savedworkouts");

    }



    public boolean saveWorkout(Workout workout){
        return true;
    }

    public List<Workout> getSavedWorkouts() {
        return savedWorkouts;
    }

    public void setSavedWorkouts(List<Workout> savedWorkouts) {
        this.savedWorkouts = savedWorkouts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addWorkout(Workout temp) {
        savedWorkouts.add(temp);
    }

    public void addSavedWorkout(Workout workout) {
        this.savedWorkouts.add(workout);
    }
}
