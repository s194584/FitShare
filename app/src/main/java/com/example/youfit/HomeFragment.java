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

import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements WorkoutDetailAdapter.OnWorkoutListener {
    private final String TAG = "HomeFragment";

   private ArrayList<Workout> workouts = new ArrayList<>();
   private WorkoutDetailAdapter mAdapter;
   private RecyclerView plannedWorkoutsRV;
   private int currentDay;
   private boolean loaded = false;
   private String curretUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class UserListener implements DatabaseListener {

        private View view;

        UserListener(View view) {
            this.view = view;
        }

        public void onStart() {
            Log.i("UsernameListener", "onStart");
        }

        public void onComplete(DataSnapshot dataSnapshot) {
            Log.i("UsernameListener", "onComplete entered");
            ArrayList<Workout> workoutsTmp = new ArrayList<>();
            for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                if (!dataValues.hasChildren()) {
                    String username = dataValues.getValue().toString();
                    curretUsername = username;
                    Log.i("UsernameListener", "on complete got username:" + curretUsername);
                    TextView welcomeBackTest = view.findViewById(R.id.welcomeBackText);
                    welcomeBackTest.setText("Welcome back " + curretUsername + "!");
                } else {
                    for (DataSnapshot workoutValues : dataValues.getChildren()) {
                        Workout workout = workoutValues.getValue(Workout.class);
                        Log.i("UsernameListener","workout name: " + workout.getName());
                        if (!workout.getName().isEmpty()) {
                            Log.i("UsernameListener",workout.getName()+" is "+workout.getRecurring().get(currentDay));
                            if (workout.getRecurring().get(currentDay)) {
                                workoutsTmp.add(workout);
                            }
                        }
                    }
                }
            }
            workouts = workoutsTmp;
            initRecyclerView(view);
        }
    }

    public void initRecyclerView(View view) {
        Log.i(TAG, "RecyclerView, workouts: " + workouts);
        plannedWorkoutsRV = (RecyclerView) view.findViewById(R.id.plannedWorkoutsRV);
        plannedWorkoutsRV.setAdapter(new WorkoutDetailAdapter(workouts, HomeFragment.this));

        plannedWorkoutsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: Does this make a difference? It currently causes crashes!
        //Make recycleView look good.
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(plannedWorkoutsRV.getContext(), RecyclerView.VERTICAL);
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
//        plannedWorkoutsRV.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Server server = ((MainActivity) getActivity()).getServer();
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2; //TODO Den burde nok sættes automatisk

        // Sunday = 1 is corrected to 6
        if(currentDay==-1){
            currentDay = 6;
        }

        //Get layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Set welcome back text
        if (!loaded) {
            Log.i("HomeFragment", "loaded: " + loaded);
            UserListener listener = new UserListener(view);
            server.loadCurrentUser(listener);
            loaded=true;
        } else {
            Log.i("HomeFragment", "loaded: " + loaded);
            Log.i("HomeFragment", "name is: " + curretUsername);
            Log.i("HomeFragment", "workouts are: " + workouts);
            TextView welcomeBackTest = view.findViewById(R.id.welcomeBackText);
            welcomeBackTest.setText("Welcome back " + curretUsername + "!");

            initRecyclerView(view);
        }

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
