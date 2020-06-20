package com.example.youfit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseElement;
import com.example.youfit.domain.ExerciseType;

import java.util.ArrayList;
import java.util.HashMap;

public class EditExerciseDialogFragment extends DialogFragment {

    private final String TAG = "EditExerciseDialog";
    private Exercise exercise;
    private ExerciseElement exerciseElement;
    private int pos;
    private String description = "";
    private Context mContext;

    // UI elements
    AutoCompleteTextView autoCompleteTextView;
    EditText amountEditText, timeTextSec, timeTextMin;
    RadioGroup radioGroup;
    Button cancelButton, saveButton;
    ViewFlipper flipper;
    View inflatedView;

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
                Log.i(TAG,"onAttach():  must implement EditExerciseDialogFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        init(inflater, container);

        // Determines type if user selects predefined exercise
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Get exercise info
                String name = (String) adapterView.getItemAtPosition(i);
                HashMap<String, ExerciseElement> hashmap = ((MainActivity) getActivity()).getHashMap().getHashMap();
                ExerciseElement exerciseElement = hashmap.get(name);

                Log.i(TAG,"Item selected. The type is: "+exerciseElement.getType());

                //Set type
                if(exerciseElement.getType().equals(ExerciseType.REPETITION.name())){
                    radioGroup.check(R.id.radiobutton_edit_exercise_reps);
                    flipper.setDisplayedChild(flipper.indexOfChild(inflatedView.findViewById(R.id.set_reps)));
                }else{
                    radioGroup.check(R.id.radiobutton_edit_exercise_time);
                    flipper.setDisplayedChild(flipper.indexOfChild(inflatedView.findViewById(R.id.set_time)));
                }

                //Lock type
                radioGroup.findViewById(R.id.radiobutton_edit_exercise_reps).setEnabled(false);
                radioGroup.findViewById(R.id.radiobutton_edit_exercise_time).setEnabled(false);
            }
        });

        // Change type
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobutton_edit_exercise_reps){
                    radioGroup.check(R.id.radiobutton_edit_exercise_reps);
                    flipper.setDisplayedChild(flipper.indexOfChild(inflatedView.findViewById(R.id.set_reps)));
                }

                if (checkedId == R.id.radiobutton_edit_exercise_time){
                    radioGroup.check(R.id.radiobutton_edit_exercise_time);
                    flipper.setDisplayedChild(flipper.indexOfChild(inflatedView.findViewById(R.id.set_time)));
                }
            }
        });

        // Cancel
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DIALOG FRAGMENT", "HIT CANCEL BUTTON");
                dismiss();
            }
        });

        // Save
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check that all inputs have been filled, else make toast:

                if (autoCompleteTextView.getText().toString().isEmpty()){
                    makeToast("Exercise name is required");
                    return;
                }

                if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_reps){
                    if (amountEditText.getText().toString().isEmpty()) {
                        makeToast("Amount cannot be empty");
                        return;
                    }
                }

                if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_time){
                    if (timeTextMin.getText().toString().isEmpty() || timeTextSec.getText().toString().isEmpty()){
                        makeToast("Minutes and seconds cannot be empty");
                    }
                }

                //Get exercise name
                String name = autoCompleteTextView.getText().toString();

                exerciseElement = ((MainActivity) mContext).getHashMap().getElement(name);

                // Check if element already exists. If not, create new element
                if (exerciseElement != null) {
                    exercise.setExerciseElement(exerciseElement);

                    if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_time){
                        long minutes = Long.parseLong(timeTextMin.getText().toString())*60000;
                        long seconds = Long.parseLong(timeTextSec.getText().toString())*1000;

                        long totalAmount = minutes + seconds;

                        exercise.setAmount(totalAmount);
                    }

                    if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_reps){
                        exercise.setAmount(Long.parseLong(amountEditText.getText().toString()));
                    }

                    listener.onDialogSave(exercise, pos);
                    dismiss();
                }
                else {

                    // Save description dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.exercise_no_exsist);
                    final EditText input = new EditText(getActivity());
                    builder.setView(input);

                    //Set dialog button for making description
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            description = input.getText().toString();

                            String type;
                            if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_reps) {
                                type = ExerciseType.REPETITION.name();
                            } else {
                                type = ExerciseType.TIME.name();
                            }

                            exerciseElement = new ExerciseElement(autoCompleteTextView.getText().toString(), type, description);
                            ((MainActivity) mContext).addPreDefinedExercise(exerciseElement);

                            if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_time){
                                long minutes = Long.parseLong(timeTextMin.getText().toString())*60000;
                                long seconds = Long.parseLong(timeTextSec.getText().toString())*1000;

                                long totalAmount = minutes + seconds;

                                exercise = new Exercise(exerciseElement, totalAmount);
                            }

                            if (radioGroup.getCheckedRadioButtonId() == R.id.radiobutton_edit_exercise_reps){
                                exercise = new Exercise(exerciseElement, Long.parseLong(amountEditText.getText().toString()));
                            }

                            listener.onDialogSave(exercise, pos);
                            dialogInterface.dismiss();
                        }
                    });

                    //Set dialog button for no description
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();
                }

                dismiss();
            }
        });

        return inflatedView;
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void init(LayoutInflater inflater, ViewGroup container){
        mContext = getActivity();
        exercise = getArguments().getParcelable("editExercise");
        pos = getArguments().getInt("position");

        //Get view
        inflatedView =inflater.inflate(R.layout.dialog_edit_exercise,container,false);

        // Auto complete setup
        ArrayList<String> workoutNames = new ArrayList<String>(((MainActivity) getActivity()).getHashMap().getHashMap().keySet());

        flipper = inflatedView.findViewById(R.id.view_flipper1);
        flipper.setDisplayedChild(flipper.indexOfChild(inflatedView.findViewById(R.id.set_time)));

        autoCompleteTextView = inflatedView.findViewById(R.id.autoedittext_edit_exercise_name);

        final ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, workoutNames);

        autoCompleteTextView.setAdapter(autocompleteAdapter);
        autoCompleteTextView.setText(exercise.retrieveName());

        radioGroup = inflatedView.findViewById(R.id.radioGroup);

        radioGroup.check(R.id.radiobutton_edit_exercise_reps);
        flipper.setDisplayedChild(flipper.indexOfChild(inflatedView.findViewById(R.id.set_reps)));

        // Amount text
        amountEditText = inflatedView.findViewById(R.id.edittext_edit_exercise_amount);
        // Buttons
        cancelButton = inflatedView.findViewById(R.id.button_edit_exercise_cancel);
        saveButton = inflatedView.findViewById(R.id.button_edit_exercise_save);

        timeTextSec = inflatedView.findViewById(R.id.edittext_edit_exercise_amount_sec);
        timeTextMin = inflatedView.findViewById(R.id.edittext_edit_exercise_amount_min);
    }

}
