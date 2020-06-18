package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;

public class ViewWorkoutDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String workoutString = "WORKOUT";

    private TextView workoutName;
    private Button startWorkoutButton;
    private Button deleteWorkoutButton;
    private boolean fromPublic;

    // TODO: Rename and change types of parameters
    private Workout mWorkout;


    public ViewWorkoutDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ViewWorkoutDetailsFragment newInstance(String param1) {
        ViewWorkoutDetailsFragment fragment = new ViewWorkoutDetailsFragment();
        Bundle args = new Bundle();
        args.putString(workoutString, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorkout = getArguments().getParcelable(workoutString);
            fromPublic = getArguments().getBoolean("public");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_workout_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startWorkoutButton = view.findViewById(R.id.start_workout_button);
        deleteWorkoutButton = view.findViewById(R.id.delete_workout_button);

        if (fromPublic) {
            deleteWorkoutButton.setVisibility(View.GONE);
        }

        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
                intent.putExtra("workout", mWorkout);
                startActivity(intent);
            }
        });

        deleteWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deletion from database
                Server server = ((MainActivity) getActivity()).getServer();
                server.removeWorkout(mWorkout);

                // go to previous fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }
}