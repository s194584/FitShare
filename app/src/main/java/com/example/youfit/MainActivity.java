package com.example.youfit;

import com.example.youfit.R.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.youfit.domain.ExerciseElement;
import com.example.youfit.domain.ExerciseElementList;
import com.example.youfit.domain.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements SignOutDialogListener, ChangePasswordDialogListener, Server.OnServerSetupCompleteListener {

    protected String TAG = "Server";

    protected boolean alreadyLoggedIn = false;
    protected Server server;
    protected ExerciseElementList exerciseElementList = new ExerciseElementList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        findViewById(id.progress_bar).setVisibility(View.VISIBLE);
        this.server = new Server(this);


        //TODO: Make waiting screen for database call back.

    }

    public void setUpNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof ChangePasswordDialogFragment){

            // Uses the Firebase build in function .sendPasswordResetEmail to reset users password.
            FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Password reset email sent!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        if (dialog instanceof SignOutDialogFragment) {
            FirebaseAuth.getInstance().signOut(); //log out
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public Server getServer() {
        return this.server;
    }

    public ExerciseElementList getHashMap()
    {
        return this.exerciseElementList;
    }

    public void addPreDefinedExercise(ExerciseElement exerciseElement){
        exerciseElementList.addElement(exerciseElement);
        server.addPreDefinedExercise(exerciseElement,exerciseElement.getName());
    }

    @Override
    public void onSetupComplete() {
        Log.i(TAG,"Setting up views.");
        setContentView(layout.activity_main);
        setUpNavigation();
        exerciseElementList.setHashMap(server.getPreDefinedExercises());
//        findViewById(id.progress_bar).setVisibility(View.GONE); //TODO
    }
}