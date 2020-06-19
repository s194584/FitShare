package com.example.youfit.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseElement implements Parcelable {

    private String name = "";
    private String type = "";
    private String description = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExerciseElement (String name, String type, String description)
    {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    protected ExerciseElement(Parcel in) {
        name = in.readString();
        type = in.readString();
        description = in.readString();
    }

    public ExerciseElement()
    {

    }

    public static final Creator<ExerciseElement> CREATOR = new Creator<ExerciseElement>() {
        @Override
        public ExerciseElement createFromParcel(Parcel in) {
            return new ExerciseElement(in);
        }

        @Override
        public ExerciseElement[] newArray(int size) {
            return new ExerciseElement[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(description);
    }


}

// TODO implement this.
// TODO Make hashmaps of names, exercise for autofill in create workout.
// TODO Add workouts and descriptions for workouts.

