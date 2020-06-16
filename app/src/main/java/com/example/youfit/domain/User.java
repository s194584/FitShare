package com.example.youfit.domain;

import java.util.List;

public class User {

    private String name;

    private List<Workout> savedWorkouts;

    public User() {}

    public User(String name) {
        this.name = name;
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
}
