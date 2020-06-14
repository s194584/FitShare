package com.example.youfit.domain;

public class Exercise {

    protected Enum<ExerciseType> type;
    protected String name;

    public Exercise(String name, Enum<ExerciseType> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Enum<ExerciseType> getType() {
        return type;
    }
}
