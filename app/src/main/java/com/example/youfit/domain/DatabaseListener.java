package com.example.youfit.domain;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;

public interface DatabaseListener {

    void onComplete(DataSnapshot dataSnapshot);

}
