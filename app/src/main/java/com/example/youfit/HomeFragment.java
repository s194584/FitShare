package com.example.youfit;

import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements WorkoutDetailAdapter.OnWorkoutListener {
    private final String TAG = "HomeFragment";

    ArrayList<Workout> workouts = new ArrayList<>();
   private WorkoutDetailAdapter mAdapter;
   private RecyclerView plannedWorkoutsRV;
   private int currentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Server server = ((MainActivity) getActivity()).getServer();
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2;
        // Sunday = 1 is corrected to 6
        if(currentDay==-1){
            currentDay = 6;
        }
        //Get layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Set welcome back text
        TextView welcomeBackTest = view.findViewById(R.id.welcomeBackText);
        String username = server.getUsername();
        welcomeBackTest.setText("Welcome back " + username + "!");

        //get workouts
        Log.i("HomeFragment", "1: Getting data and current day is: "+currentDay);
        workouts = server.getCurrentUsersWorkouts();

        //Get recycler view
        Log.i("HomeFragment", "2: Getting recyclerView");
        plannedWorkoutsRV = (RecyclerView) view.findViewById(R.id.plannedWorkoutsRV);
        if(plannedWorkoutsRV == null)
        {
            Log.i("HomeFragment", "ERROR: Could not find recyclerView");
        }

        //make adapter with sample data

        Log.i("HomeFragment", "3: Making adapter");
        if(mAdapter == null)
        {
            Log.i("HomeFragment", "ERROR: Could not create adapter");
        }

        //add adapter to recycle view
        Log.i("HomeFragment", "3: Adding adapter to recycler view");
        assert plannedWorkoutsRV != null;
        DatabaseReference userWorkoutsRef = FirebaseDatabase.getInstance().getReference("Users/" + server.getUserUID() + "/savedWorkouts");
        userWorkoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG,"Data updated");
                workouts.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Workout workout = ds.getValue(Workout.class);
                    Log.i(TAG,workout.getName()+" is "+workout.getRecurring().get(currentDay));
                    if (workout.getRecurring().get(currentDay)) {
                        workouts.add(workout);
                    }
                }
                plannedWorkoutsRV.setAdapter(new WorkoutDetailAdapter(workouts, HomeFragment.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG,"Could not retrieve workouts.");
            }
        });

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
        Log.i("HomeFragment", "A workout has been clicked: " + workout.getName());
        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", workouts.get(position));
        bundle.putBoolean("public", false);
        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_HomeFragment_to_ViewWorkoutDetailsFragment, bundle);
    }

    @Override
    public void onButtonClick(int position) {
        Workout workout = workouts.get(position);
        Log.i("HomeFragment", "A workout has been started: " + workout.getName());
        Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
        intent.putExtra("workout", workout);
        startActivity(intent);

    }

}
