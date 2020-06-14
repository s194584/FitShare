package com.example.youfit.domain;

public class Exercise {

    protected Enum<ExerciseType> type;
    protected String name;
    protected String reps = null;
    protected String time = null;

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

    public int getTime() {
        return Integer.parseInt(time);
    }
}
