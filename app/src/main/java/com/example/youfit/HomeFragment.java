package com.example.youfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.User;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<Workout> workouts = new ArrayList<Workout>();
    User user = new User("This means smth is wrong", ""+0);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView welcomeBackTest = view.findViewById(R.id.welcomeBackText);
        welcomeBackTest.setText("Welcome back " + user.getName() + "!");

        Log.i("HomeFragment", "1: Getting recyclerView");
        //Get recycler view
        RecyclerView  plannedWorkoutsRV = (RecyclerView) view.findViewById(R.id.plannedWorkoutsRV);
        if(plannedWorkoutsRV == null)
        {
            Log.i("HomeFragment", "ERROR: Could not find recyclerView");
        }

        Log.i("HomeFragment", "2: Creating example data");
        // Create example data
        workouts.add(new Workout("1", new ArrayList<Exercise>()));
        workouts.add(new Workout("2", new ArrayList<Exercise>()));
        workouts.add(new Workout("3", new ArrayList<Exercise>()));
        workouts.add(new Workout("4", new ArrayList<Exercise>()));
        workouts.add(new Workout("5", new ArrayList<Exercise>()));

        Log.i("HomeFragment", "3: Making adapter");
        //make adapter with sample data
        WorkoutDetailAdapter adapter = new WorkoutDetailAdapter(workouts);
        if(adapter == null)
        {
            Log.i("HomeFragment", "ERROR: Could not create adapter");
        }

        Log.i("HomeFragment", "4: Adding adapter to recycler view");
        //add adapter to recycle view
        plannedWorkoutsRV.setAdapter(adapter);


        Log.i("HomeFragment", "5: Set recycler views layout manager");
        //Set layoutmanager
        plannedWorkoutsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(plannedWorkoutsRV.getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        plannedWorkoutsRV.addItemDecoration(dividerItemDecoration);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.createWorkoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_createWorkoutFragment);
            }
        });

    }

}
