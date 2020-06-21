package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class BrowsePrivateWorkoutsFragment extends Fragment implements BrowseWorkoutDetailAdapter.OnWorkoutListener, DatabaseListener {

    private static final String TAG = "BrowsePrivateFragment";
    private ArrayList<Workout> workouts = new ArrayList<Workout>();
    private View view;

    public void onStart() {
        super.onStart();

        initRecyclerView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_browse_private_workouts, container, false);

        initRecyclerView(view);

        return view;
    }

    @Override
    public void onComplete(DataSnapshot dataSnapshot) {
        Log.i(TAG, "on complete entered:");
        ArrayList<Workout> workoutstmp = new ArrayList<Workout>();
        for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
            Workout workout = dataValues.getValue(Workout.class);
            if (!workout.getName().isEmpty()) {
                workoutstmp.add(workout);
            }
        }
        workouts = workoutstmp;
        Log.i(TAG, "workouts are: " + workouts);
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView privateWorkoutsRV = view.findViewById(R.id.privateWorkoutsRV);
        BrowseWorkoutDetailAdapter adapter = new BrowseWorkoutDetailAdapter(getContext(), workouts,this);
        privateWorkoutsRV.setAdapter(adapter);
        privateWorkoutsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onWorkoutClick(int position) {
        Log.i(TAG, "A workout has been clicked: " + workouts.get(position).getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", workouts.get(position));
        bundle.putBoolean("public", false);
        NavHostFragment.findNavController(BrowsePrivateWorkoutsFragment.this)
                .navigate(R.id.action_browseWorkoutsFragment_to_viewWorkoutDetailsFragment, bundle);
    }

    @Override
    public void onButtonClick(int position) {
        Log.i(TAG, "BUTTON CLICKED: " + workouts.get(position).getName());

        Workout workout = workouts.get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
        intent.putExtra("workout", workout);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate ");
        Server server = ((MainActivity) getActivity()).getServer();
        server.loadCurrentUsersWorkouts(this);
    }
}