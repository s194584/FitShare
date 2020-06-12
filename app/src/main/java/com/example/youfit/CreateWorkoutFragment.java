package com.example.youfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class CreateWorkoutFragment extends Fragment {

    NavController navController;
    protected TextInputEditText inputExcersize;
    protected String inputExcersizeString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(CreateWorkoutFragment.this);

        this.inputExcersize = view.findViewById(R.id.inputExcersize);


        Button nextBtn = view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputExcersizeString = inputExcersize.getText().toString();
                if(!TextUtils.isEmpty(inputExcersizeString)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("excersize",inputExcersizeString);
                    NavHostFragment.findNavController(CreateWorkoutFragment.this)
                            .navigate(R.id.action_createWorkoutFragment_to_excersizeFragment,bundle);

                } else {
                    Toast.makeText(v.getContext(),
                            "Please enter a form of excersize",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}