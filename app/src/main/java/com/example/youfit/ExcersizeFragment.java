package com.example.youfit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;
import com.example.youfit.domain.Workout;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExcersizeFragment extends Fragment {

    private String workoutSelected;
    private String excersizeSelected;
    private LinearLayout parentLinearLayout;
    protected HashMap<Enum<ExerciseType>,List<String>> excersizeStrings = new HashMap<>();
    protected List<String> excersizeStringsList = new ArrayList<String>();
    protected Workout currentWorkout;
    protected ArrayList<Exercise> exercises = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_excersize, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.workoutSelected = getArguments().getString("excersize");
        this.currentWorkout = new Workout(this.workoutSelected);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView excersizeSelectedTextView = view.findViewById(R.id.excersizeSelectedTextView);
        excersizeSelectedTextView.setText(workoutSelected);

        this.parentLinearLayout = view.findViewById(R.id.excersizeLinearLayout);

        this.excersizeStrings.put(ExerciseType.REPETIOTION, Arrays.asList(getResources().getStringArray(R.array.rep_excersizes)));
        this.excersizeStrings.put(ExerciseType.TIME, Arrays.asList(getResources().getStringArray(R.array.time_excersizes)));

        for (List<String> list : this.excersizeStrings.values()) {
            excersizeStringsList.addAll(list);
        }

        Button addExcersizeBtn = view.findViewById(R.id.addExerciseBtn);

        addExcersizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add excersize");
                // inflate view
                View viewInflated = LayoutInflater.from(getContext()).
                        inflate(R.layout.popup_input_excersize,
                                (ViewGroup) getView(), false);
                // Set up the input
                final AutoCompleteTextView inputExcersize = viewInflated.findViewById(R.id.inputExcersize);

                //set up autocomplete adapter
                ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<String>(getContext(),
                        R.layout.support_simple_spinner_dropdown_item, excersizeStringsList);
                inputExcersize.setAdapter(autocompleteAdapter);


                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        excersizeSelected = inputExcersize.getText().toString();

                        if (excersizeStrings.get(ExerciseType.REPETIOTION).contains(excersizeSelected)) {
                            iflateLayout();

                            //make excersize and add it to the currentWorkout.
                            Exercise exercise = new Exercise(excersizeSelected,ExerciseType.REPETIOTION);
                            exercises.add(exercise);

                        } else if (excersizeStrings.get(ExerciseType.TIME).contains(excersizeSelected)) {
                            iflateLayout();

                            Exercise exercise = new Exercise(excersizeSelected,ExerciseType.TIME);
                            exercises.add(exercise);
                        } else {
                            Toast.makeText(getContext(),
                                    "Please enter a vaild workout form",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    private void iflateLayout() {
                        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView=inflater.inflate(R.layout.list_of_excersizes_detail, null);
                        ((TextView) rowView.findViewById(R.id.excersizeNameTextView)).setText(excersizeSelected);
                        // Add the new row.
                        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
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

    @Override
    public void onStop() {
        this.currentWorkout.setExercises(this.exercises);
        super.onStop();
    }
}