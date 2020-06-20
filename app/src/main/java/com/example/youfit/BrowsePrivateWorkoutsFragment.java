package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.ChildDatabaseListener;
import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class BrowsePrivateWorkoutsFragment extends Fragment implements BrowseWorkoutDetailAdapter.OnWorkoutListener {

    private static final String TAG = "BrowsePrivateFragment";
    private ArrayList<Workout> workouts = new ArrayList<Workout>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_browse_private_workouts, container, false);

        Log.i("BrowsePrivateFragment", "2: Creating example data");

        // Load workouts from server
        CurrentUserWorkouts listener = new CurrentUserWorkouts(view,this);
        Server server = ((MainActivity) getActivity()).getServer();
        server.loadCurrentUsersWorkouts2(listener);

        // Inflate the layout for this fragment
        return view;
    }

    private class CurrentUserWorkouts implements DatabaseListener {

        private View view;
        BrowsePrivateWorkoutsFragment browsePrivateWorkoutsFragment;

        CurrentUserWorkouts(View view, BrowsePrivateWorkoutsFragment browsePrivateWorkoutsFragment) {
            this.view = view;
            this.browsePrivateWorkoutsFragment = browsePrivateWorkoutsFragment;
        }

        public void onStart() {
            Log.i("CurrentUserWorkouts", "onStart");
        }

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


    }



//    private class PrivateWorkoutListener implements ChildDatabaseListener {
//
//        private View view;
//
//        PrivateWorkoutListener(View view) {
//            this.view = view;
//        }
//
//        @Override
//        public void onChildAddedCompleted(DataSnapshot dataSnapshot) {
//            Workout workout = dataSnapshot.getValue(Workout.class);
//            workouts.add(workout);
//            initRecyclerView(view);
//        }
//
//        @Override
//        public void onChildChangedCompleted(DataSnapshot dataSnapshot) {
//            Workout workout = dataSnapshot.getValue(Workout.class);
//            workouts.(workout);
//            initRecyclerView(view);
//        }
//
//        @Override
//        public void onChildRemovedCompleted(DataSnapshot dataSnapshot) {
//            Workout workout = dataSnapshot.getValue(Workout.class);
//            workouts.remove(workout);
//            initRecyclerView(view);
//        }
//    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView privateWorkoutsRV = view.findViewById(R.id.privateWorkoutsRV);
        BrowseWorkoutDetailAdapter adapter = new BrowseWorkoutDetailAdapter(getContext(), workouts,this);
        privateWorkoutsRV.setAdapter(adapter);
        privateWorkoutsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onWorkoutClick(int position) {
        Log.i("BrowsePublicFragment", "A workout has been clicked: " + workouts.get(position).getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", workouts.get(position));
        bundle.putBoolean("public", false);
        NavHostFragment.findNavController(BrowsePrivateWorkoutsFragment.this)
                .navigate(R.id.action_browseWorkoutsFragment_to_viewWorkoutDetailsFragment, bundle);
    }

    @Override
    public void onButtonClick(int position) {
        Log.i("BrowsePublicFragment", "BUTTON CLICKED: " + workouts.get(position).getName());

        Workout workout = workouts.get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
        intent.putExtra("workout", workout);
        startActivity(intent);
    }
}