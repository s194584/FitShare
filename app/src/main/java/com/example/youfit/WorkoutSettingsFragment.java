package com.example.youfit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;
import com.example.youfit.domain.WorkoutDifficulty;
import com.example.youfit.domain.WorkoutType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutSettingsFragment extends Fragment {

    private String TAG = "WorkoutSettingsFragment";
    Workout currentWorkout;
    LinearLayout recurringChecks;
    String existingWorkoutKey;
    EditText descriptionText;
    Spinner typeSpinner;
    Spinner difficultySpinner;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments()!=null){
            currentWorkout = getArguments().getParcelable("newWorkout");
            existingWorkoutKey = getArguments().getString("key");
        } else{
            currentWorkout = new Workout();
        }

        return inflater.inflate(R.layout.fragment_workout_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setting Views based on currentWorkout
        EditText ed = getActivity().findViewById(R.id.edittext_workout_name_settings);
        ed.setText(currentWorkout.getName());

        recurringChecks = getActivity().findViewById(R.id.recurringgroup_workout_settings);
        ArrayList<Boolean> tempRecurring = currentWorkout.getRecurring();
        for(int i = 0; i < tempRecurring.size(); i++){
            ((CheckBox) recurringChecks.getChildAt(i)).setChecked(tempRecurring.get(i));
        }

        typeSpinner = ((Spinner) getActivity().findViewById(R.id.typespinner));
        WorkoutType[] workoutTypeAsEnums = WorkoutType.values();
        String[] workoutTypes = new String[workoutTypeAsEnums.length];
        for (int i=0; i<workoutTypeAsEnums.length; i++) {
            workoutTypes[i] = Utility.formatEnum(workoutTypeAsEnums[i].toString());
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, workoutTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        setSelectedType();

        difficultySpinner = ((Spinner) getActivity().findViewById(R.id.difficultyspinner));
        WorkoutDifficulty[] workoutDifficultiesAsEnums = WorkoutDifficulty.values();
        String[] workoutDifficulties = new String[workoutDifficultiesAsEnums.length];
        for (int i=0; i<workoutDifficultiesAsEnums.length; i++) {
            workoutDifficulties[i] = Utility.formatEnum(workoutDifficultiesAsEnums[i].toString());
        }

        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, workoutDifficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        setSelectedDifficulty();

        descriptionText = ((EditText) getActivity().findViewById(R.id.edittext_description));
        descriptionText.setText(currentWorkout.getDescription());

        //set public button
        ((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).setChecked(currentWorkout.getPublicWorkout());


        // Complete button listener
        getActivity().findViewById(R.id.button_workout_settings_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWorkout = saveWorkout(currentWorkout);
                Server server = ((MainActivity) getActivity()).getServer();

                if (existingWorkoutKey.isEmpty()) {
                    server.addWorkout(currentWorkout);
                } else {
                    server.changeWorkout(currentWorkout, existingWorkoutKey);
                }

                NavHostFragment.findNavController(WorkoutSettingsFragment.this).popBackStack(R.id.HomeFragment,false);
            }
        });

        // Back button listener
        getActivity().findViewById(R.id.button_workout_settings_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWorkout = saveWorkout(currentWorkout);
                Bundle bundle = new Bundle();
                bundle.putParcelable("newWorkout",currentWorkout);
                bundle.putBoolean("isWorkoutExisting", false);
                bundle.putString("key", existingWorkoutKey);
                NavHostFragment.findNavController(WorkoutSettingsFragment.this).navigate(R.id.action_workoutSettingsFragment_to_workoutFragment,bundle);
            }
        });
    }

    private Workout saveWorkout(Workout workout){
        List<Boolean> tempRecurring = (Arrays.asList(new Boolean[recurringChecks.getChildCount()]));
        for(int i = 0; i < recurringChecks.getChildCount(); i++){
            tempRecurring.set(i, ((CheckBox) recurringChecks.getChildAt(i)).isChecked());
        }

        workout.setWorkoutType(typeSpinner.getSelectedItem().toString().toUpperCase());
        workout.setWorkoutDifficulty(difficultySpinner.getSelectedItem().toString().toUpperCase());
        workout.setRecurring(new ArrayList<Boolean>(tempRecurring));
        workout.setPublicWorkout(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).isChecked());
        workout.setDescription(descriptionText.getText().toString());
        workout.setCreator(((MainActivity) getActivity()).getServer().getUsername());
        return workout;
    }

    public void setSelectedType() {
        if (currentWorkout.getWorkoutType() != null) {
            switch (WorkoutType.valueOf(currentWorkout.getWorkoutType())) {
                case STRENGTH:
                    typeSpinner.setSelection(0);
                    break;
                case CARDIO:
                    typeSpinner.setSelection(1);
                    break;
                default:
                    typeSpinner.setSelection(2);
            }
        } else {
            typeSpinner.setSelection(2);
        }
    }

    public void setSelectedDifficulty() {
        Log.d(TAG, "setSelectedDifficulty: tried: ");
        if (currentWorkout.getWorkoutType() != null) {
            switch (WorkoutDifficulty.valueOf(currentWorkout.getWorkoutDifficulty())) {
                case BEGINNER:
                    difficultySpinner.setSelection(0);
                    break;
                case MEDIUM:
                    difficultySpinner.setSelection(1);
                    break;
                case HARD:
                    difficultySpinner.setSelection(2);
                    break;
                case EXTREME:
                    difficultySpinner.setSelection(3);
                    break;
                default:
                    difficultySpinner.setSelection(4);
            }
        }
    }

    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
    }
}
