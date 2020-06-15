package com.example.youfit.domain;

public class Exercise {

    protected Enum<ExerciseType> type;
    protected String name;
    protected int reps = 0;
    protected String time = null;

    public Exercise(String name) {
        this.name = name;
        this.type = ExerciseType.REPETITION;
    }

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


    public int getReps() {
        return reps;
    }
    public int getTime() {
        return Integer.parseInt(time);
    }
}
