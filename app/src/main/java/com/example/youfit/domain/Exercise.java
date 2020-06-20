package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Exercise implements Parcelable {

    private ExerciseElement exerciseElement;
    private long amount = 0;

    public Exercise() { } //Empty constructor for firebase

    public Exercise(Exercise exercise)
    {
        this.exerciseElement = exercise.exerciseElement;
        this.amount = Integer.parseInt(exercise.retrieveAmountString());

        Log.i("Exercise/constructor", "created an exercise from an exercise");
    }

    public Exercise(String name) //This is for pauses
    {
        Log.i("Exercise", "A PAUSE WAS CREATED");
        this.exerciseElement = new ExerciseElement(name, "PAUSE","You take a breather.");
        this.amount = 0;

        Log.i("Exercise/constructor", "created a pause");

    }

    public Exercise(ExerciseElement exerciseElement, long amount) {
        this.exerciseElement = exerciseElement;
        this.amount = amount;

        Log.i("Exercise/constructor", "created an exercise from exerciseElement and amount");

    }

    public Exercise(ExerciseElement exerciseElement) {
        this.exerciseElement = exerciseElement;

        Log.i("Exercise/constructor", "created an exercise from an exerciseElement");

    }

    protected Exercise(Parcel in) {
        exerciseElement = in.readParcelable(ExerciseElement.class.getClassLoader());
        amount = in.readLong();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public String retrieveAmountString(){
        return ""+amount;
    }

    public long getAmount()
    {
        return amount;
    }

    public String repsOrTime(){
        if(exerciseElement.getType().equals(ExerciseType.REPETITION.name())){
            return "Reps:";
        }
        return "Time:";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(exerciseElement, flags);
        dest.writeLong(amount);
    }

    public String retrieveName()
    {
        return exerciseElement.getName();
    }

    public String retrieveType() {
        return exerciseElement.getType();
    }

    public ExerciseElement getExerciseElement() {
        return exerciseElement;
    }

    public void setExerciseElement(ExerciseElement exerciseElement)
    {
        this.exerciseElement = exerciseElement;
    }

    public void setAmount(long amount)
    {
        this.amount = amount;
    }
}
