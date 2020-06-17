package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.youfit.databinding.ActivityLoginBinding;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialogListener{

    ActivityLoginBinding mBinding;

    protected EditText editTextEmailAddress, editTextPassword, editTextPasswordConfirm;
    protected Button buttonSignin;
    protected ProgressBar progressBarSignIn;
    protected TextView textViewCreateNewProfile;
    protected TextView forgotPassword;

    protected FirebaseAuth firebaseAuth;
    protected FirebaseDatabase rootNode;
    protected DatabaseReference databaseReference;

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
        this.forgotPassword = findViewById(R.id.textViewForgotPassword2);

        this.firebaseAuth = FirebaseAuth.getInstance(); //get current instance of Firebase Auth.

        //check if user is already signed in/up
        if(firebaseAuth.getCurrentUser() != null) {
            goToMainActivity();
        }

        // Forgot password dialog
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog();
            }
        });

    }

    private void showResetDialog(){
        ForgotPasswordDialog forgot = new ForgotPasswordDialog();
        forgot.show(getSupportFragmentManager(), "reset_password");
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    //click event for signin/signup button
    public void onSignIn(View view) {
        //Check validity of input, and give corresponding error message.
        if (checkIfEmpty(this.editTextEmailAddress,"Email") ||
                checkIfEmpty(this.editTextPassword,"Password")) {
            return;
        }

        //load strings
        final String mEmail = this.editTextEmailAddress.getText().toString().trim();
        final String mPassword = this.editTextPassword.getText().toString().trim();

        if(this.mBinding.getSignInMode()) { //sign in event

            //set loading bar visible
            this.progressBarSignIn.setVisibility(View.VISIBLE);

            //check if user exists
            this.firebaseAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    onSuccesfullTask(task, "Welcome!");
                    if (task.isSuccessful()) { //TODO: Remove this if statement. When Christians shit virker!
                        goToMainActivity();
                    }

                }
            });
        } else { //sign up event

            //load confirmPassword String
            String mConfirmPassword = this.editTextPasswordConfirm.getText().toString().trim();

            //More validity checks
            if (checkIfEmpty(this.editTextPasswordConfirm,"Password")) {
                return;
            }
            if (!mPassword.equals(mConfirmPassword)) {
                this.editTextPasswordConfirm.setError("Passwords must match");
                return;
            }

            //set loading bar visible
            this.progressBarSignIn.setVisibility(View.VISIBLE);

            //registration of user
            this.firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    onSuccesfullTask(task, "Successfully created user!");

                    //load new user into database
                    if (task.isSuccessful()) {
                        String userID = firebaseAuth.getCurrentUser().getUid();
                        rootNode = FirebaseDatabase.getInstance();
                        databaseReference = rootNode.getReference("Users"); //get reference to database.

                        User user = new User("New User");
                        databaseReference.child(userID).setValue(user);
                    }


                }
            });


        }
    }


    private void onSuccesfullTask(Task<AuthResult> task, String succesMessage) {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        if (task.isSuccessful()) {
            toast.setText(succesMessage);
            goToMainActivity();
        } else {
            toast.setText("Error: " + task.getException().getMessage());
            progressBarSignIn.setVisibility(View.INVISIBLE);
        }
        toast.show();
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

    // Confirm reset password button click on forgot-password-dialog
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, EditText email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
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

    // Cancel button click on forgot-password-dialog
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}