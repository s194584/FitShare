package com.example.youfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseFragment extends Fragment {

    private String workoutSelected;
    private String exerciseSelected;

    // New recycle view
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public ArrayList<Exercise> exercises = new ArrayList<>();
    protected Workout currentWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.workoutSelected = getArguments().getString("newWorkout");
        this.currentWorkout = new Workout(this.workoutSelected);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView exerciseSelectedTextView = view.findViewById(R.id.workoutSelectedTextView);
        exerciseSelectedTextView.setText(workoutSelected);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.list_exercise);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ExersiceAdapter(exercises);
        recyclerView.setAdapter(mAdapter);
        
        getActivity().findViewById(R.id.button_workout_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWorkout.setExercises(exercises);
                Log.i("EXERCISEFRAGMENT","WORKOUT CREATED");

                // TODO - ADD TO USER AND FINISH TASK

            }
        });

        Button addExcersizeBtn = view.findViewById(R.id.addExerciseBtn);

        addExcersizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add exercise");
                // inflate view
                View viewInflated = LayoutInflater.from(getContext()).
                        inflate(R.layout.popup_input_exercise,
                                (ViewGroup) getView(), false);
                // Set up the input
                final AutoCompleteTextView inputExercise = viewInflated.findViewById(R.id.inputExcersize);

                //set up autocomplete adapter
                ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(getContext(),
                        R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.rep_exercises));
                inputExercise.setAdapter(autocompleteAdapter);


                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        exerciseSelected = inputExercise.getText().toString();

//                        if (exerciseStrings.get(ExerciseType.REPETITION).contains(exerciseSelected)) {
//                            //make excersize and add it to the currentWorkout.
//                            Exercise exercise = new Exercise(exerciseSelected,ExerciseType.REPETITION);
//                            exercises.add(exercise);
//
//                        } else if (exerciseStrings.get(ExerciseType.TIME).contains(exerciseSelected)) {
//                            Exercise exercise = new Exercise(exerciseSelected,ExerciseType.TIME);
//                            exercises.add(exercise);
//                        } else {
//                            Toast.makeText(getContext(),
//                                    "Please enter a vaild workout form",
//                                    Toast.LENGTH_SHORT).show();
//                        }
                        exercises.add(new Exercise(exerciseSelected));
                        mAdapter.notifyItemInserted(exercises.size()-1);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }
}