package com.example.youfit.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;

    private List<Workout> savedWorkouts = new ArrayList<>();

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public User(String name, List<Workout> savedWorkouts) {
        this.name = name;
        this.savedWorkouts = savedWorkouts;
    }

    public void addSavedWorkout(Workout workout) {
        this.savedWorkouts.add(workout);
    }

    public boolean saveWorkout(Workout workout){
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Workout> getSavedWorkouts() {
        return savedWorkouts;
    }

    public void setSavedWorkouts(List<Workout> savedWorkouts) {
        this.savedWorkouts = savedWorkouts;
    }

    public void addWorkout(Workout temp) {
        savedWorkouts.add(temp);
    }
}
