package com.example.youfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.youfit.databinding.ActivityLoginBinding;

public class LoginActivity extends Activity {

    ActivityLoginBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this ,R.layout.activity_login);

        mBinding.setSignInMode(true);

        // Sign in
        findViewById(R.id.buttonSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.textViewCreateNewProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.setSignInMode(!mBinding.getSignInMode());
            }
        });

        findViewById(R.id.textViewCreateNewProfile);

    }
}