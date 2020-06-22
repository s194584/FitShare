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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Server {

    protected String TAG = "Server";

    protected FirebaseAuth firebaseAuth;
    protected FirebaseDatabase rootNode;
    protected Activity activity;

    private String username;
    private OnServerSetupCompleteListener onServerSetupCompleteListener;
    protected HashMap<String, Workout> publicWorkouts = new HashMap<>();
    protected HashMap<String, Workout> currentUsersWorkouts = new HashMap<>();
    protected HashMap<String, ExerciseElement> preDefinedExercises = new HashMap<>();

    protected ProgressDialog loadingDialog;

    public interface OnServerSetupCompleteListener {
        void onSetupComplete();
    }


    public Server(Activity activity) {
        onServerSetupCompleteListener = (OnServerSetupCompleteListener) activity;
        this.activity = activity;
        setLoadingDialog();
        loadPreDefinedExercises();
    }

    public String getUsername() {
        return (this.username != null) ? this.username : "could not finde username";
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

    public void dismissLoadingDialog() {
        if (this.loadingDialog!=null) {
            this.loadingDialog.dismiss();
        }
    }


    public void updateCurrentUserUsername(String name) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/name");

            this.username=name;
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

            if(workout.getPublicWorkout()) {
                addPublicWorkouts(workout);
            }

            databaseReference.child(key).setValue(workout);
        }
    }

    private void addPublicWorkouts(Workout workout) {
        DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

        databaseReference.child(workout.getUniqueID()).setValue(workout); // add workout with same key as Users workout
    }

    public void removeWorkout(Workout workout) {
        setLoadingDialog();

        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            if (workout.getPublicWorkout()) {
                removePublicWorkouts(workout);
            }

            databaseReference.child(workout.getUniqueID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    loadingDialog.dismiss();
                }
            });
        }
    }

    private void removePublicWorkouts(Workout workout) {
        DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

        databaseReference.child(workout.getUniqueID()).removeValue();
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
                changePublicWorkout(key, workout);
            } else { //if workout was puplic remove it
                removePublicWorkouts(workout);
            }

            databaseReference.child(key).setValue(workout);
        }
    }

    private void changePublicWorkout(String key, Workout workout) {
        this.publicWorkouts.put(key, workout);
        DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");
        databaseReference.child(key).setValue(workout);
    }

    public void loadCurrentUsersWorkouts(final DatabaseListener listener) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/savedWorkouts");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listener.onComplete(dataSnapshot);
                    if(!activity.hasWindowFocus()) { // check if on startup
                        onServerSetupCompleteListener.onSetupComplete();
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void loadPublicWorkouts(final DatabaseListener listener) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("PublicWorkouts");

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

    public void changeNotifications(Boolean notification) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/notifications");

            databaseReference.setValue(notification);
        }
    }

    public void loadUserNotifications(final DatabaseListener listener) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/notifications");

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

    public void changeStats(Statistics stats) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/statistics");

            databaseReference.setValue(stats);
        }
    }

    public void loadUserStats(final DatabaseListener listener) {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid() + "/statistics");

            Log.i(TAG, "onDataChange: Loading stats");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i(TAG, "onDataChange: Loaded stats");
                    listener.onComplete(dataSnapshot);
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
                    username = dataSnapshot.child("name").getValue().toString();

                    databaseListener.onComplete(dataSnapshot);

//                    loadingDialog.dismiss();

                    Log.w(TAG, "userLoaded");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "load user:cancelled", databaseError.toException());
                }
            });
            Log.w(TAG, "After on DataChange");
        }
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
                    preDefinedExercises.put(dataSnapshot.getKey(), exercise);
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