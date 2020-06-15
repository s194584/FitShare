package com.example.youfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.youfit.domain.Workout;

public class WorkoutSettingsFragment extends Fragment {

    Workout currentWorkout;
    LinearLayout recurringChecks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getArguments()!=null){
            currentWorkout = (Workout) getArguments().getParcelable("newWorkout");
        } else{
            currentWorkout = new Workout();
        }
        return inflater.inflate(R.layout.fragment_workout_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recurringChecks = getActivity().findViewById(R.id.recurringgroup_workout_settings);

        EditText ed = getActivity().findViewById(R.id.edittext_workout_name_settings);
        ed.setText(currentWorkout.getName());

        // Setting Views based on currentWorkout
        boolean[] tempRecurring = currentWorkout.getRecurring();
        for(int i = 0; i < recurringChecks.getChildCount(); i++){
            ((CheckBox) recurringChecks.getChildAt(i)).setChecked(tempRecurring[i]);
        }
        ((ToggleButton) getActivity().findViewById(R.id.toggle_workout_noticifations)).setChecked(currentWorkout.isNotifications());
        ((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).setChecked(currentWorkout.isPublicWorkout());



        getActivity().findViewById(R.id.button_workout_settings_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                boolean[] tempRecurring = new boolean[recurringChecks.getChildCount()];
                for(int i = 0; i < recurringChecks.getChildCount(); i++){
                    tempRecurring[i] = ((CheckBox) recurringChecks.getChildAt(i)).isChecked();
                }
                currentWorkout.setRecurring(tempRecurring);
                currentWorkout.setNotifications(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_noticifations)).isChecked());
                currentWorkout.setPublicWorkout(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).isChecked());


                bundle.putParcelable("newWorkout",currentWorkout);
                NavHostFragment.findNavController(WorkoutSettingsFragment.this).navigate(R.id.action_workoutSettingsFragment_to_workoutFragment,bundle);
            }
        });

    }

    private Workout saveWorkout(Workout workout){
        boolean[] tempRecurring = new boolean[recurringChecks.getChildCount()];
        for(int i = 0; i < recurringChecks.getChildCount(); i++){
            tempRecurring[i] = ((CheckBox) recurringChecks.getChildAt(i)).isChecked();
        }
        workout.setRecurring(tempRecurring);
        workout.setNotifications(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_noticifations)).isChecked());
        workout.setPublicWorkout(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).isChecked());
        return workout;
    }

}
