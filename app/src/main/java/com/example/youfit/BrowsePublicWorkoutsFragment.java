package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;


public class BrowsePublicWorkoutsFragment extends Fragment implements BrowseWorkoutDetailAdapter.OnWorkoutListener {

    private static final String TAG = "BrowsePublicFragment";
    ArrayList<Workout> workouts = new ArrayList<Workout>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "1: Creating inflator");
        View view = inflater.inflate(R.layout.fragment_browse_public_workouts, container, false);

        Log.i(TAG, "2: Getting data from firebase");
        Server server = ((MainActivity) getActivity()).getServer();
        workouts = server.getPublicWorkouts();

        Log.i(TAG, "3: Initiating recyclerview");
        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.publicWorkoutsRV);
        BrowseWorkoutDetailAdapter adapter = new BrowseWorkoutDetailAdapter(getContext(), workouts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onWorkoutClick(int position) {
        Log.i(TAG, "A workout has been clicked: " + workouts.get(position).getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", workouts.get(position));
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
        startActivity(intent);
    }
}