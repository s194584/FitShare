package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {

    protected ExerciseType type;
    protected String name;
    protected int reps = 0;
    protected String time = null;

    public Exercise(String name) {
        this.name = name;
        this.type = ExerciseType.REPETITION;
    }

    public Exercise(String name, ExerciseType type) {
        this.name = name;
        this.type = type;
    }

    // Parcel constructor
    protected Exercise(Parcel in) {
        name = in.readString();
        reps = in.readInt();
        time = in.readString();
    }




    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ExerciseType getType() {
        return type;
    }


    public int getReps() {
        return reps;
    }
    public int getTime() {
        return Integer.parseInt(time);
    }
    public String getAmount(){
        if(type == ExerciseType.REPETITION){
            return ""+reps;
        }
        return time;
    }
    public String repsOrTime(){
        if(type == ExerciseType.REPETITION){
            return "Reps:";
        }
        return "Time:";
    }


    // Parcelable part
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(reps);
        parcel.writeString(time);
    }
}
