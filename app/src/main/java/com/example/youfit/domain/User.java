package com.example.youfit.domain;

import java.util.List;

public class User {

    private String name;
    private final String uniqueID;

    private List<Workout> savedWorkouts;


    public User(String name, String uniqueID) {
        this.name = name;
        this.uniqueID = uniqueID;
    }


    public boolean saveWorkout(Workout workout){
        return true;
    }

    public String getName()
    {
        return name;
    }
}
