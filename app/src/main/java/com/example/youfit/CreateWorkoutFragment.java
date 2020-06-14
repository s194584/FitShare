package com.example.youfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.youfit.domain.Workout;
import com.google.android.material.textfield.TextInputEditText;

public class CreateWorkoutFragment extends Fragment {

    NavController navController;
    protected TextInputEditText inputWorkout;
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

        this.inputWorkout = view.findViewById(R.id.inputWorkout);


        Button nextBtn = view.findViewById(R.id.createWorkoutBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputWorkoutName = inputWorkout.getText().toString();
                if(!TextUtils.isEmpty(inputWorkoutName)) {

                    Bundle bundle = new Bundle();
                    bundle.putString("newWorkout",inputWorkoutName);
                    NavHostFragment.findNavController(CreateWorkoutFragment.this)
                            .navigate(R.id.action_createWorkoutFragment_to_exerciseFragment,bundle);

                } else {
                    Toast.makeText(v.getContext(),
                            "Please enter a name for your workout",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}