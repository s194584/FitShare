package com.example.youfit.domain;

import android.util.Log;

import java.util.HashMap;

public class ExerciseElementList {

    HashMap<String, ExerciseElement> elementHashMap;

    public ExerciseElementList ()
    {
        elementHashMap = new HashMap<>();
        Log.i("ExerciseElementList", "Created new exerciseElementList");
    }

    public HashMap getElementHashMap()
    {
        return elementHashMap;
    }

    public void addElement(ExerciseElement exerciseElement)
    {
        elementHashMap.put(exerciseElement.getName(), exerciseElement);
        Log.i("ExerciseElementList", "added element: " + exerciseElement.getName() + " to hashMap");
    }

    public void setElementHashMap(HashMap<String, ExerciseElement> elementHashMap) {
        this.elementHashMap = elementHashMap;
    }

    public ExerciseElement getElement(String name)
    {
        return elementHashMap.get(name);
    }
}
