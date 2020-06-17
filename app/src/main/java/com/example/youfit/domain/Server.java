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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Server {

    protected String TAG = "Server";

    protected FirebaseAuth firebaseAuth;
    protected FirebaseDatabase rootNode;
    protected DatabaseReference databaseReference;
    protected Activity activity;

    protected User currentUser;

    //Firebase key --> Workout
    protected HashMap<String,Workout> publicWorkouts = new HashMap<>();
    protected HashMap<String,Workout> currentUsersWorkouts = new HashMap<>();

//    protected ArrayList<Workout> publicWorkouts = new ArrayList<>();
//    protected ArrayList<Workout> currentUsersWorkouts = new ArrayList<>();

    public Server(Activity activity) {
        this.activity = activity;
        loadCurrentUser();
        loadPublicWorkouts();
    }

    public String getUsername() {
        return (this.currentUser!=null) ? currentUser.getName() : "could not finde username";
    }

    public ArrayList<Workout> getPublicWorkouts() {
        ArrayList<Workout> publicWorkouts = getAllPublicWorkouts();
        ArrayList<Workout> currentUsersWorkouts = getCurrentUsersWorkouts();

        ArrayList<Workout> resultWorkouts = new ArrayList<>();
        for (Workout workout: publicWorkouts) {
            if (currentUsersWorkouts.contains(workout)) {
                continue;
            }
            resultWorkouts.add(workout);
        }
        return resultWorkouts;
    }

    public ArrayList<Workout> getCurrentUsersWorkouts() {
        return (this.currentUser!= null) ? new ArrayList<Workout>(currentUsersWorkouts.values()) : new ArrayList<Workout>();
    }

    private ArrayList<Workout> getAllPublicWorkouts() {
        return (this.currentUser!=null) ?  new ArrayList<Workout>(publicWorkouts.values()) : new ArrayList<Workout>();
    }

    public void addWorkout(Workout workout) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            databaseReference.push().setValue(workout);
        }

    }

    public void removeWorkout(Workout workout) {
        String key = "";
        for (Map.Entry<String, Workout> entry : this.currentUsersWorkouts.entrySet()) {
            if (Objects.equals(workout, entry.getValue())) {
                key = (entry.getKey());
                break;
            }
        }

        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            databaseReference.child(key).removeValue();
        }

    }

    public void updateCurrentUserUsername (String name) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/name");

            this.currentUser.setName(name);
            databaseReference.setValue(name);
        }
    }

    private void loadCurrentUsersWorkouts() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.w(TAG, "CurrentUser is not null");
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid()+ "/savedWorkouts");

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    if (workout.isPublicWorkout()) {
                        addPublicWorkouts(workout,dataSnapshot.getKey());
                    }
                    currentUsersWorkouts.put(dataSnapshot.getKey(),workout);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    if (workout.isPublicWorkout()) {
                        removePublicWorkouts(workout,dataSnapshot.getKey());
                    }
                    currentUsersWorkouts.remove(dataSnapshot.getKey());
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void removePublicWorkouts(Workout workout, String key) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

            databaseReference.child(key).removeValue();
        }
    }

    private void addPublicWorkouts(Workout workout, String key) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

//            this.publicWorkouts.add(workout); //TODO: is this really nessesary?
            databaseReference.child(key).setValue(workout); // add workout with same key as Users workout
        }
    }


    private void loadPublicWorkouts() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.w(TAG, "CurrentUser is not null");
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    publicWorkouts.put(dataSnapshot.getKey(),workout);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    publicWorkouts.remove(dataSnapshot.getKey());
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    private void loadCurrentUser() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.w(TAG, "CurrentUser is not null");
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid());


            // Attach a listener to read the data at our user reference
            databaseReference.addListenerForSingleValueEvent ( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User usertmp = new User();

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                            if (!dataValues.hasChildren()) {
                                usertmp.setName(dataValues.getValue().toString());
                            } else {
                                Workout workout = dataValues.getValue(Workout.class);
                                if (!workout.getName().isEmpty()) {
                                    currentUsersWorkouts.put(dataSnapshot.getKey(),workout);
                                    usertmp.addWorkout(workout);
                                }
                            }
                        }
//                        User user = dataSnapshot.getValue(User.class);
                        currentUser = usertmp;
                        loadCurrentUsersWorkouts();

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
