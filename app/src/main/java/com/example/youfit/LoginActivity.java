package com.example.youfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.youfit.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {

    ActivityLoginBinding mBinding;

    protected EditText editTextEmailAddress, editTextPassword,editTextPasswordConfirm;
    protected Button buttonSignin;
    protected ProgressBar progressBarSignIn;
    protected TextView textViewCreateNewProfile;

    protected FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this ,R.layout.activity_login);
        mBinding.setSignInMode(true);

        //load components
        this.editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        this.buttonSignin = findViewById(R.id.buttonSignin);
        this.textViewCreateNewProfile = findViewById(R.id.textViewCreateNewProfile);
        this.progressBarSignIn = findViewById(R.id.progressBarSignIn);

        this.firebaseAuth = FirebaseAuth.getInstance(); //get current instance of database.

        if(firebaseAuth.getCurrentUser() != null) {
            goToMainActivity();
        }


    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    //click event for signin/signup button
    public void onSignIn(View view) {
        if(this.mBinding.getSignInMode()) { //sign in event
            goToMainActivity();
        } else { //sign up event

            //Check validity of input, and give corresponding error message.
            if (checkIfEmpty(this.editTextEmailAddress,"Email") &&
                    checkIfEmpty(this.editTextPassword,"Password") &&
                    checkIfEmpty(this.editTextPasswordConfirm,"Password")) {
                return;
            }

            //load strings
            String mEmail = this.editTextEmailAddress.getText().toString().trim();
            String mPassword = this.editTextPassword.getText().toString().trim();
            String mConfirmPassword = this.editTextPasswordConfirm.getText().toString().trim();

            //More validity checks
            if (!mPassword.equals(mConfirmPassword)) {
                this.editTextPasswordConfirm.setError("Passwords must match");
            }


            this.progressBarSignIn.setVisibility(View.VISIBLE);


            //registration of user
            this.firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                    if (task.isSuccessful()) {
                        toast.setText("Successfully created user!");
                        goToMainActivity();
                    } else {
                        toast.setText("Error: " + task.getException().getMessage());
                    }
                    toast.show();
                }
            });


        }
    }

    private boolean checkIfEmpty(EditText editText, String requriedMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(requriedMessage + " is required");
            return true;
        }
        return false;
    }

    //click event for create new profile/back textview
    public void onCreateNewProfile(View view) {
        mBinding.setSignInMode(!mBinding.getSignInMode());
    }
}