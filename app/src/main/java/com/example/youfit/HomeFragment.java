package com.example.youfit;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.User;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements WorkoutDetailAdapter.OnWorkoutListener {
    private final String TAG = "HomeFragment";

    ArrayList<Workout> workouts = new ArrayList<>();
   private WorkoutDetailAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Server server = ((MainActivity) getActivity()).getServer();
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        // Sunday = 0 is corrected to 7
        if(currentDay==0){
            currentDay = 7;
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Set welcome back text

        TextView welcomeBackTest = view.findViewById(R.id.welcomeBackText);
        String username = server.getUsername();
        welcomeBackTest.setText("Welcome back " + username + "!");
        workouts = server.getCurrentUsersWorkouts();


        //Get recycler view
        Log.i("HomeFragment", "1: Getting recyclerView");
        RecyclerView  plannedWorkoutsRV = (RecyclerView) view.findViewById(R.id.plannedWorkoutsRV);
        if(plannedWorkoutsRV == null)
        {
            Log.i("HomeFragment", "ERROR: Could not find recyclerView");
        }

        // Create example data
        Log.i("HomeFragment", "2: Creating example data");

        //TODO Get Firebase data and fill in here
        workouts.add(new Workout("1", new ArrayList<Exercise>()));
        workouts.add(new Workout("2", new ArrayList<Exercise>()));
        workouts.add(new Workout("3", new ArrayList<Exercise>()));
        workouts.add(new Workout("4", new ArrayList<Exercise>()));
        workouts.add(new Workout("5", new ArrayList<Exercise>()));
        Exercise pause = new Exercise("Pause", "Pause");
        pause.setTime(30000); // 30 sec
        Exercise burpees = new Exercise("Burpees", "Time");
        burpees.setTime(600000); // 10 min
        for (Workout w : workouts){
            w.addExercise(new Exercise("Crunches", "Time"));
            w.addExercise(new Exercise("Pull ups", "Repetition"));
            w.addExercise(new Exercise("Push ups", "Repetition"));
            w.addExercise(pause);
            w.addExercise(new Exercise("Stretching", "Repetition"));
            w.addExercise(burpees);
        }

        //make adapter with sample data
        Log.i("HomeFragment", "3: Making adapter");
        mAdapter = new WorkoutDetailAdapter(workouts, this,currentDay);
        if(mAdapter == null)
        {
            Log.i("HomeFragment", "ERROR: Could not create adapter");
        }

        //add adapter to recycle view
        Log.i("HomeFragment", "3: Adding adapter to recycler view");
        assert plannedWorkoutsRV != null;
        plannedWorkoutsRV.setAdapter(mAdapter);

        //Set layoutmanager
        Log.i("HomeFragment", "4: Set recycler views layout manager");
        plannedWorkoutsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Make recycleView look good.
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
                        .navigate(R.id.action_HomeFragment_to_workoutFragment);
            }
        });
    }

    @Override
    public void onWorkoutClick(int position) {
        Workout workout = workouts.get(position);
        Log.i("HomeFragment", "A workout has been staarted");
        //intent intent = new Intent(this, xxx.java); //TODO show preview
        //intent.putExtra("workout", workout);
        //startActivity(intent);
    }

    @Override
    public void onButtonClick(int position) {

        Workout workout = workouts.get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
        intent.putExtra("workout", workout);
        startActivity(intent);

    }

}
