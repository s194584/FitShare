package com.example.youfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;

public class EditExerciseDialogFragment extends DialogFragment {
    private final String TAG = "EditExerciseDialog";
    // Exercise
    private Exercise exercise;
    private int pos;

    // UI elements
    AutoCompleteTextView autoCompleteTextView;
    EditText amountEditText;
    RadioGroup radioGroup;
    Button cancelButton, saveButton;

    public interface EditExerciseDialogFragmentListener{
        public void onDialogSave(Exercise exercise, int position);
    }

    EditExerciseDialogFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (EditExerciseDialogFragmentListener) getTargetFragment();
        } catch(ClassCastException e){
                Log.i("EditExerciseDialog","onAttach():  must implement EditExerciseDialogFragmentListener");
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        exercise = getArguments().getParcelable("editExercise");
        pos = getArguments().getInt("position");

        Log.i(TAG,exercise.toString());
        View inflatedView =inflater.inflate(R.layout.dialog_edit_exercise,container,false);

        // Auto complete setup
        autoCompleteTextView = inflatedView.findViewById(R.id.autoedittext_edit_exercise_name);
        ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.rep_exercises));
        autoCompleteTextView.setAdapter(autocompleteAdapter);
        autoCompleteTextView.setText(exercise.getName());

        // RadioGroup
        radioGroup = inflatedView.findViewById(R.id.radioGroup);

        if(exercise.getType().equals(ExerciseType.REPETITION.name())){
            radioGroup.check(R.id.radiobutton_edit_exercise_reps);
        }else{
            radioGroup.check(R.id.radiobutton_edit_exercise_time);
        }

        // Amount text
        amountEditText = inflatedView.findViewById(R.id.edittext_edit_exercise_amount);
        // Buttons
        cancelButton = inflatedView.findViewById(R.id.button_edit_exercise_cancel);
        saveButton = inflatedView.findViewById(R.id.button_edit_exercise_save);

        // Button onClick()
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DIALOG FRAGMENT", "HIT SAVE BUTTON");
                getDialog().dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check that all inputs have been filled:
                if(autoCompleteTextView.getText().toString().isEmpty() || amountEditText.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(),"Not all fields has been filled.",Toast.LENGTH_SHORT);
                    return;
                }

                exercise.setName(autoCompleteTextView.getText().toString());
                if(radioGroup.getCheckedRadioButtonId()==R.id.radiobutton_edit_exercise_reps){
                    exercise.setType(ExerciseType.REPETITION.name());
                    exercise.setReps(Integer.parseInt(amountEditText.getText().toString()));
                }else{
                    exercise.setType(ExerciseType.TIME.name());
                    exercise.setTime(Long.parseLong(amountEditText.getText().toString()));
                }

                listener.onDialogSave(exercise,pos);
                getDialog().dismiss();
            }
        });


        return inflatedView;
    }

//    @NonNull
//    @Override
//    public Dialog onDialogCreate(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        dialogBuilder.setMessage(R.string.add_exercise);
//
//        // Get the custom View
//        View inflatedView = requireActivity().getLayoutInflater().inflate(R.layout.dialog_edit_exercise,null);
//
//        // Auto complete setup
//        final AutoCompleteTextView inputName = inflatedView.findViewById(R.id.autoedittext_edit_exercise_name);
//        ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(getContext(),
//                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.rep_exercises));
//        inputName.setAdapter(autocompleteAdapter);
//
//        dialogBuilder.setView(inflatedView);
//        // Action for "YES" button
//        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                listener.onDialogPositiveClick(EditExerciseDialogFragment.this);
//            }
//        });
//
//        // Action for "NO" button
//        dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                listener.onDialogNegativeClick(EditExerciseDialogFragment.this);
//            }
//        });
//
//        return dialogBuilder.create();
//
//    }

}
