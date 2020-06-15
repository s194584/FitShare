package com.example.youfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.youfit.domain.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailActivity extends Activity{

    protected EditText newEmail;
    protected Button confirmNewEmail;
    protected FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_email);

        newEmail = findViewById(R.id.newEmail);
        confirmNewEmail = findViewById(R.id.confirmNewEmail);

        firebaseAuth = FirebaseAuth.getInstance();

        confirmNewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfEmpty(newEmail, "Email")){
                    return;
                    // Missing check for actual email address
                } else {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.updateEmail(String.valueOf(newEmail.getText()));

                    user.sendEmailVerification();

                    Toast.makeText(getApplicationContext(), "Email verification sent", Toast.LENGTH_LONG).show();

                    Intent finishChangeEmail = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(finishChangeEmail);
                    finish();
                }
            }
        });

    }

    private boolean checkIfEmpty(EditText editText, String requiredMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(requiredMessage + " is required");
            return true;
        }
        return false;
    }
}
