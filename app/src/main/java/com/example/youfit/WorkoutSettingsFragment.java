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

import com.example.youfit.domain.Server;
import com.example.youfit.domain.User;
import com.example.youfit.domain.Workout;
import com.example.youfit.domain.WorkoutType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        ArrayList<Boolean> tempRecurring = currentWorkout.getRecurring();
        for(int i = 0; i < tempRecurring.size(); i++){
            ((CheckBox) recurringChecks.getChildAt(i)).setChecked(tempRecurring.get(i));
        }
        ((ToggleButton) getActivity().findViewById(R.id.toggle_workout_noticifations)).setChecked(currentWorkout.isNotifications());
        ((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).setChecked(currentWorkout.isPublicWorkout());

        getActivity().findViewById(R.id.button_workout_settings_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWorkout = saveWorkout(currentWorkout);
                Server server = ((MainActivity) getActivity()).getServer();
                server.addWorkout(currentWorkout);
//                User user = server.getCurrentUser();
//                user.addSavedWorkout(currentWorkout);
//                server.updateCurrentUser(user);

                NavHostFragment.findNavController(WorkoutSettingsFragment.this).navigate(R.id.action_workoutSettingsFragment_to_HomeFragment);
            }
        });
        getActivity().findViewById(R.id.button_workout_settings_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                ArrayList<Boolean> tempRecurring = new ArrayList<Boolean>(recurringChecks.getChildCount());
                for(int i = 0; i < recurringChecks.getChildCount(); i++){
                    tempRecurring.set(i, ((CheckBox) recurringChecks.getChildAt(i)).isChecked());
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
        List<Boolean> tempRecurring = (Arrays.asList(new Boolean[recurringChecks.getChildCount()]));
        for(int i = 0; i < recurringChecks.getChildCount(); i++){
            tempRecurring.set(i, ((CheckBox) recurringChecks.getChildAt(i)).isChecked());
        }

        workout.setWorkoutType(WorkoutType.DEFAULT.name()); //TODO: Replace with what ever Type is choosen REMEBER to user .name() as it is stored as a string.
        workout.setRecurring(new ArrayList<Boolean>(tempRecurring));
        workout.setNotifications(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_noticifations)).isChecked());
        workout.setPublicWorkout(((ToggleButton) getActivity().findViewById(R.id.toggle_workout_public)).isChecked());
        return workout;
    }

}
