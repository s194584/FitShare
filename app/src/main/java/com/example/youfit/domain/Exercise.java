package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {

    private ExerciseElement exerciseElement;
    protected String type;
    protected String name;
    protected int reps = 0;
    protected long time = 0;

    public Exercise() { }

    public Exercise(Exercise exercise) {
        this.type= exercise.type;
        this.name = exercise.name;
        this.reps = exercise.reps;
        this.time = exercise.time;
    }

    public Exercise(String name) {
        this.name = name;
        this.type = ExerciseType.REPETITION.name();
    }

    public Exercise(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Parcel constructor
    protected Exercise(Parcel in) {
        name = in.readString();
        type = in.readString();
        reps = in.readInt();
        time = in.readLong();
    }

//    public String getTypeString(E) {
//
//    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getReps() {
        return reps;
    }
    public long getTime() {
        return time;
    }
    public String getAmount(){
        if(type.equals(ExerciseType.REPETITION.name())){
            return ""+reps;
        }
        return ""+time;
    }
    public String repsOrTime(){
        if(type.equals(ExerciseType.REPETITION.name())){
            return "Reps:";
        }
        return "Time:";
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setTime(long time) {
        this.time = time;
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
        parcel.writeString(type);
        parcel.writeInt(reps);
        parcel.writeLong(time);
    }
}
