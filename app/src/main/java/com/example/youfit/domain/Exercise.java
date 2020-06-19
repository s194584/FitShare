package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {

    private ExerciseElement exerciseElement;
    private long amount = 0;

    public Exercise() { }

    public Exercise(Exercise exercise)
    {
        this.exerciseElement = exercise.exerciseElement;
        this.amount = Integer.parseInt(exercise.getAmountString());
    }

    public Exercise(String name) //This is for pauses
    {
        this.exerciseElement = null;
        this.amount = 0;
    }

    public Exercise(ExerciseElement exerciseElement, long amount) {
        this.exerciseElement = exerciseElement;
        this.amount = amount;
    }

    public Exercise(ExerciseElement exerciseElement) {
        this.exerciseElement = exerciseElement;
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

    public String getAmountString(){

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

    public String getName()
    {
        return exerciseElement.getName();
    }

    public String getType() {
        return exerciseElement.getType();
    }

    public ExerciseElement getExerciseElement() {
        return exerciseElement;
    }

    public void setExerciseElement(ExerciseElement exerciseElement)
    {
        this.exerciseElement = exerciseElement;
    }

    public void setAmount(String amount)
    {
        this.amount = Long.parseLong(amount);
    }
}
