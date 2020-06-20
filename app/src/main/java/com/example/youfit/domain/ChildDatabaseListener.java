package com.example.youfit.domain;

import com.google.firebase.database.DataSnapshot;

public interface ChildDatabaseListener {

    void onChildAddedCompleted(DataSnapshot dataSnapshot);
    void onChildChangedCompleted(DataSnapshot dataSnapshot);
    void onChildRemovedCompleted(DataSnapshot dataSnapshot);

}
