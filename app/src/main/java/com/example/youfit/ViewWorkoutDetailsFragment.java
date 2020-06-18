package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;

public class ViewWorkoutDetailsFragment extends Fragment {

    private static final String workoutString = "WORKOUT";
    private static final String TAG = "ViewWorkoutDetailsFrag";

    private TextView workoutName;
    private Button startWorkoutButton;
    private Button deleteWorkoutButton;
    private Button editWorkoutButton;
    private boolean fromPublic;

    private Workout mWorkout;
    ArrayList<Exercise> exercises = new ArrayList<Exercise>();


    public ViewWorkoutDetailsFragment() {
        // Required empty public constructor
    }

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
        editWorkoutButton = view.findViewById(R.id.edit_workout_button);

        exercises = mWorkout.getExercises();

        if (fromPublic) {
            deleteWorkoutButton.setVisibility(View.GONE);
            editWorkoutButton.setVisibility(View.GONE);
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

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.exercisedetailsRV);
        ClickWorkoutDetailAdapter adapter = new ClickWorkoutDetailAdapter(getContext(), exercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}