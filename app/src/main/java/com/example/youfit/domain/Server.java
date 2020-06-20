package com.example.youfit.domain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    protected String TAG = "Server";

    protected FirebaseAuth firebaseAuth;
    protected FirebaseDatabase rootNode;
    protected Activity activity;

    protected User currentUser;
    private OnServerSetupCompleteListener onServerSetupCompleteListener;
    protected HashMap<String, Workout> publicWorkouts = new HashMap<>();
    protected HashMap<String, Workout> currentUsersWorkouts = new HashMap<>();
    protected HashMap<String, ExerciseElement> preDefinedExercises = new HashMap<>();

    protected ProgressDialog loadingDialog;

//    protected ArrayList<Workout> publicWorkouts = new ArrayList<>();
//    protected ArrayList<Workout> currentUsersWorkouts = new ArrayList<>();

    public interface OnServerSetupCompleteListener
    {
        void onSetupComplete();
    }


    public Server(Activity activity) {
        onServerSetupCompleteListener = (OnServerSetupCompleteListener) activity;
        this.activity = activity;
        setLoadingDialog();
        loadPublicWorkouts();
        loadPreDefinedExercises();
    }

    public String getUsername() {
        return (this.currentUser != null) ? currentUser.getName() : "could not finde username";
    }

    public String getUserUID() {
        return this.firebaseAuth.getCurrentUser().getUid();
    }

    public ArrayList<Workout> getPublicWorkouts() {
        ArrayList<Workout> res = new ArrayList<>();

        for (String key : publicWorkouts.keySet()) {
            if (!currentUsersWorkouts.containsKey(key)) {
                res.add(publicWorkouts.get(key));
            }
        }

        return res;
    }

    public ArrayList<Workout> getCurrentUsersWorkouts() {
        return (this.currentUser != null) ? new ArrayList<Workout>(currentUsersWorkouts.values()) : new ArrayList<Workout>();
    }

    private ArrayList<Workout> getAllPublicWorkouts() {
        return (this.currentUser != null) ? new ArrayList<Workout>(publicWorkouts.values()) : new ArrayList<Workout>();
    }

    public void setLoadingDialog() {
        this.loadingDialog = new ProgressDialog(activity);
        this.loadingDialog.setMessage("Application is loading");
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setProgress(0);
        this.loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        this.loadingDialog.show();
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


    public void addWorkout(Workout workout) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");


            String key = databaseReference.push().getKey();
            workout.setUniqueID(key);
            databaseReference.child(key).setValue(workout);
        }

    }

    public void removeWorkout(Workout workout) {
        setLoadingDialog();

        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            databaseReference.child(workout.getUniqueID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    loadingDialog.dismiss();
                }
            });
        }

    }

    public void changeWorkout(Workout workout, String key) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            //Make sure overwriting workout also has the correct key
            workout.setUniqueID(key);

            if (this.currentUsersWorkouts.containsKey(key)) {
                this.currentUsersWorkouts.put(key, workout);
            }
            if (workout.getPublicWorkout()) {
                changePuplicWorkout(key, workout);
            }

            databaseReference.child(key).setValue(workout);
        }
    }


    public void loadCurrentUsersWorkouts() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    if (workout.getPublicWorkout()) {
                        addPublicWorkouts(workout, dataSnapshot.getKey());
                    }
                    currentUsersWorkouts.put(dataSnapshot.getKey(), workout);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    if (workout.getPublicWorkout()) {
                        removePublicWorkouts(workout, dataSnapshot.getKey());
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

    public void loadCurrentUsersWorkouts2(final DatabaseListener listener) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid()+ "/savedWorkouts");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listener.onComplete(dataSnapshot);
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

            databaseReference.child(key).setValue(workout); // add workout with same key as Users workout
        }
    }

    private void changePuplicWorkout(String key, Workout workout) {
        this.publicWorkouts.put(key, workout);
        DatabaseReference databaseReference = this.rootNode.getReference("PuplicWorkouts");
        databaseReference.child(key).setValue(workout);
    }


    private void loadPublicWorkouts() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i(TAG, "Done loading initial data");
                    onServerSetupCompleteListener.onSetupComplete();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    publicWorkouts.put(dataSnapshot.getKey(), workout);
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


    public void loadCurrentUser(final DatabaseListener databaseListener) {
        Log.w(TAG, "Starting to load server");
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.w(TAG, "CurrentUser is not null");
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid());

            // Attach a listener to read the data at our user reference
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.w(TAG, "userLoaded_1");
                    User usertmp = new User();

                    if (dataSnapshot.exists()) {
                        databaseListener.onComplete(dataSnapshot);
                        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                            if (!dataValues.hasChildren()) {
                                usertmp.setName(dataValues.getValue().toString());
                            } else {
                                Workout workout = dataValues.getValue(Workout.class);
                                if (!workout.getName().isEmpty()) {
                                    currentUsersWorkouts.put(dataSnapshot.getKey(), workout);
                                    usertmp.addWorkout(workout);
                                }
                            }
                        }
                        currentUser = usertmp;
                        loadCurrentUsersWorkouts();
                        loadingDialog.dismiss();

                        Log.w(TAG, "userLoaded");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "load user:cancelled", databaseError.toException());
                }
            });
            Log.w(TAG, "After on DataChange");
        }
    }


    private void loadScreenCreator() {
        this.rootNode = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = this.rootNode.getReference();


    }

    private void loadPreDefinedExercises() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("DefinedExercises");

//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //TODO CLEANUP, is this still relevant?
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Log.i(TAG,"Done loading initial data");
////                    onServerSetupCompleteListener.onSetupComplete();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ExerciseElement exercise = dataSnapshot.getValue(ExerciseElement.class);
                    preDefinedExercises.put(dataSnapshot.getKey(),exercise);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    preDefinedExercises.remove(dataSnapshot.getKey());
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

    public void addPreDefinedExercise(ExerciseElement exerciseElement, String key) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("DefinedExercises");

            databaseReference.child(key).setValue(exerciseElement); // add workout with same key as Users workout
        }
    }
    public void removePreDefinedExercise(ExerciseElement exerciseElement, String key) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("DefinedExercises");

            databaseReference.child(key).removeValue();
        }
    }

    public HashMap<String, ExerciseElement> getPreDefinedExercises() {
        return preDefinedExercises;
    }
}