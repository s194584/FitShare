package com.example.youfit.domain;

import java.util.HashMap;

public class ExerciseElementList {

    HashMap<String, ExerciseElement> hashMap;

    public ExerciseElementList ()
    {
        hashMap = new HashMap<>();
        //TODO add all those fancy precoded exercises, dammmmnnnn.
    }

    public HashMap getHashMap()
    {
        return hashMap;
    }

    public void addElement(ExerciseElement exerciseElement)
    {
        hashMap.put(exerciseElement.getName(), exerciseElement);
    }

    public ExerciseElement getElement(String name)
    {
        return hashMap.get(name);
    }
}
