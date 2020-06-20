package com.example.youfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.youfit.domain.Exercise;

public class ExerciseInfoDialogFragment extends DialogFragment {

    Exercise exercise;

    public static ExerciseInfoDialogFragment newInstance(Exercise exercise)
    {
        ExerciseInfoDialogFragment fragment = new ExerciseInfoDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("exercise", exercise);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exercise = getArguments().getParcelable("exercise");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(exercise.getExerciseElement().getDescription())
                .setCancelable(false)
                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
