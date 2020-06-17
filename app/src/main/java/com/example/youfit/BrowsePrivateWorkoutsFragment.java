package com.example.youfit;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowsePrivateWorkoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowsePrivateWorkoutsFragment extends Fragment implements BrowseWorkoutDetailAdapter.OnWorkoutListener {

    private static final String TAG = "BrowsePrivateFragment";
    ArrayList<Workout> workouts = new ArrayList<Workout>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_browse_private_workouts, container, false);

        Log.i("BrowsePrivateFragment", "2: Creating example data");
        // Create example data
        workouts.add(new Workout("MYcoolWorkout", new ArrayList<Exercise>()));
        workouts.add(new Workout("Strong", new ArrayList<Exercise>()));
        workouts.add(new Workout("boii", new ArrayList<Exercise>()));
        workouts.add(new Workout("Running", new ArrayList<Exercise>()));
        workouts.add(new Workout("quick one", new ArrayList<Exercise>()));
        workouts.add(new Workout("wadup", new ArrayList<Exercise>()));
        workouts.add(new Workout("okaeh", new ArrayList<Exercise>()));
        workouts.add(new Workout("5x5", new ArrayList<Exercise>()));
        workouts.add(new Workout("NO EXCUSES", new ArrayList<Exercise>()));

        initRecyclerView(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.privateWorkoutsRV);
        BrowseWorkoutDetailAdapter adapter = new BrowseWorkoutDetailAdapter(getContext(), workouts,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onWorkoutClick(int position) {
        Toast.makeText(getContext(), "Clicked: " + workouts.get(position).getName(), Toast.LENGTH_SHORT).show();
        Log.i("BrowsePublicFragment", "A workout has been clicked: " + workouts.get(position).getName());
        //Intent intent = new Intent(this, XXX.java); //TODO Display workout activity here
        //intent.putExtra("workout", workout);
        //startActivity(intent);
    }
}