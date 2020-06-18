package com.example.youfit.domain;

public class ExerciseElement {

    private final String name;
    private final String type;
    private final String description;

    public ExerciseElement (String name, String type, String description)
    {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

// TODO implement this.
// TODO Make hashmaps of names, exercise for autofill in create workout.
// TODO Add workouts and descriptions for workouts.
