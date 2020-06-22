package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;
import com.example.youfit.domain.WorkoutDifficulty;
import com.example.youfit.domain.WorkoutType;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;


public class BrowsePublicWorkoutsFragment extends Fragment implements OnWorkoutListener, DatabaseListener {

    private static final String TAG = "BrowsePublicFragment";
    private final int RESULT_CODE_DOWORKOUT = 214;

    ArrayList<Workout> workouts = new ArrayList<Workout>();
    private View view;
    private BrowseWorkoutDetailAdapter mAdapter;
    private String difficultyFilter = "";
    private String typeFilter = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "1: Creating inflator");
        this.view = inflater.inflate(R.layout.fragment_browse_public_workouts, container, false);


        Log.i(TAG, "2: Initiating recyclerview");
        initRecyclerView(view);

        difficultyFilter = getString(R.string.difficulty_filter);
        typeFilter = getString(R.string.type_filter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner filterType = view.findViewById(R.id.spinner_filter_type);
        WorkoutType[] workoutTypeAsEnums = WorkoutType.values();
        String[] workoutTypes = new String[workoutTypeAsEnums.length+1];
        workoutTypes[0] = getResources().getString(R.string.type_filter);
        for (int i=1; i<workoutTypeAsEnums.length+1; i++) {
            workoutTypes[i] = Utility.formatEnum(workoutTypeAsEnums[i-1].toString());
        }


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, workoutTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterType.setAdapter(typeAdapter);


        final Spinner filterDiff = view.findViewById(R.id.spinner_filter_diff);
        WorkoutDifficulty[] workoutDifficultiesAsEnums = WorkoutDifficulty.values();
        String[] workoutDifficulties = new String[workoutDifficultiesAsEnums.length+1];
        workoutDifficulties[0] = getResources().getString(R.string.difficulty_filter);
        for (int i=1; i<workoutDifficultiesAsEnums.length+1; i++) {
            workoutDifficulties[i] = Utility.formatEnum(workoutDifficultiesAsEnums[i-1].toString());
        }


        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, workoutDifficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterDiff.setAdapter(difficultyAdapter);


        filterDiff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                difficultyFilter = (String) adapterView.getItemAtPosition(i);
                mAdapter.filter(difficultyFilter,typeFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeFilter = (String) adapterView.getItemAtPosition(i);
                mAdapter.filter(difficultyFilter,typeFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        view.findViewById(R.id.button_clear_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType.setSelection(0);
                filterDiff.setSelection(0);
                difficultyFilter = getString(R.string.difficulty_filter);
                typeFilter = getString(R.string.type_filter);
                mAdapter.filter(difficultyFilter,typeFilter);
            }
        });

    }

    @Override
    public void onComplete(DataSnapshot dataSnapshot) {
        Log.i("CurrentUserWorkouts", "on complete got username:");
        ArrayList<Workout> workoutstmp = new ArrayList<Workout>();
        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
            Workout workout = dataValues.getValue(Workout.class);
            if (!workout.getName().isEmpty()) {
                workoutstmp.add(workout);
            }
        }
        workouts = workoutstmp;
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.publicWorkoutsRV);
        mAdapter = new BrowseWorkoutDetailAdapter(workouts, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onWorkoutClick(int position) {
        Log.i(TAG, "A workout has been clicked: " + mAdapter.getmFilteredWorkouts().get(position).getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", mAdapter.getmFilteredWorkouts().get(position));
        bundle.putBoolean("public", true);
        Log.i(TAG, "Navigating from " + NavHostFragment.findNavController(this).getCurrentDestination());
        NavHostFragment.findNavController(BrowsePublicWorkoutsFragment.this)
                .navigate(R.id.action_browseWorkoutsFragment_to_viewWorkoutDetailsFragment, bundle);
    }

    @Override
    public void onButtonClick(int position) {
        Log.i(TAG, "A button has been clicked: " + workouts.get(position).getName());

        Workout workout = workouts.get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
        intent.putExtra("workout", workout);
        getActivity().startActivityForResult(intent,RESULT_CODE_DOWORKOUT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load workouts from server
        Server server = ((MainActivity) getActivity()).getServer();
        server.loadPublicWorkouts(this);
    }
}