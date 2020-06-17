package com.example.youfit.domain;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Server {

    protected String TAG = "Server";

    protected FirebaseAuth firebaseAuth;
    protected FirebaseDatabase rootNode;
    protected DatabaseReference databaseReference;
    protected Activity activity;

    protected User currentUser;
    protected ArrayList<Workout> currentUsersWorkouts = new ArrayList<>();

    public Server(Activity activity) {
        this.activity = activity;
        loadCurrentUser();
    }

    public String getUsername() {
        if (this.currentUser!=null) {
            return currentUser.getName();
        } else {
            return "could not finde username";
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void updateCurrentUser(User user) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        String userID = firebaseAuth.getCurrentUser().getUid();
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Users"); //get reference to database.
        databaseReference.child(userID).setValue(user);
    }

    public void addWorkout(Workout workout) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            databaseReference.push().setValue(workout);
        }

    }

    public void updateCurrentUserUsername (String name) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/name");

            databaseReference.setValue(name);
        }
    }


    public void loadCurrentUser() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.w(TAG, "CurrentUser is not null");
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid());


            // Attach a listener to read the data at our user reference
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Workout> currentUsersWorkoutstmp = new ArrayList<>();
                    User usertmp = new User();

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                            if (!dataValues.hasChildren()) {
                                usertmp.setName(dataValues.getValue().toString());
                            } else {
                                Workout workout = dataValues.getValue(Workout.class);
                                usertmp.addWorkout(workout);
                            }
                        }
//                        User user = dataSnapshot.getValue(User.class);
                        currentUser = usertmp;

                        Log.w(TAG, "userLoaded");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "load user:cancelled", databaseError.toException());
                }
            });
        }
    }


}
