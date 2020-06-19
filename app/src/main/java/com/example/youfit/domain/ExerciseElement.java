package com.example.youfit.domain;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ExerciseElement implements Parcelable {

    private String name = "";
    private String type = "";
    private String description = "";

    public ExerciseElement() //For firebase
    {

    }

    @SuppressLint("LongLogTag")
    public ExerciseElement (String name, String type, String description)
    {
        this.name = name;
        this.type = type;
        this.description = description;

        Log.i("ExerciseElement/constructor", "created an exerciseElement from name, type and description");

    }

    protected ExerciseElement(Parcel in) {
        name = in.readString();
        type = in.readString();
        description = in.readString();

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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
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


