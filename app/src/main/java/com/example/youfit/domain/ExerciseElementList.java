package com.example.youfit.domain;

import android.util.Log;

import java.util.HashMap;

public class ExerciseElementList {

    HashMap<String, ExerciseElement> hashMap;

    public ExerciseElementList ()
    {
        hashMap = new HashMap<>();
        Log.i("ExerciseElementList", "Created new exerciseElementList");
    }

    public HashMap getHashMap()
    {
        return hashMap;
    }

    public void addElement(ExerciseElement exerciseElement)
    {
        hashMap.put(exerciseElement.getName(), exerciseElement);
        Log.i("ExerciseElementList", "added element: " + exerciseElement.getName() + " to hashMap");
    }

    public void setHashMap(HashMap<String, ExerciseElement> hashMap) {
        this.hashMap = hashMap;
    }

    public ExerciseElement getElement(String name)
    {
        return hashMap.get(name);
    }
}
