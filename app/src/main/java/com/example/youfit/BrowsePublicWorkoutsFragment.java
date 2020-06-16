package com.example.youfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowsePublicWorkoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowsePublicWorkoutsFragment extends Fragment implements BrowseWorkoutDetailAdapter.OnWorkoutListener {

    private static final String TAG = "BrowsePublicFragment";
    ArrayList<Workout> workouts = new ArrayList<Workout>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_browse_public_workouts, container, false);

        Log.i("BrowsePrivateFragment", "2: Creating example data");
        // Create example data
        workouts.add(new Workout("1", new ArrayList<Exercise>()));
        workouts.add(new Workout("2", new ArrayList<Exercise>()));
        workouts.add(new Workout("3", new ArrayList<Exercise>()));
        workouts.add(new Workout("4", new ArrayList<Exercise>()));
        workouts.add(new Workout("5", new ArrayList<Exercise>()));

        initRecyclerView(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.publicWorkoutsRV);
        BrowseWorkoutDetailAdapter adapter = new BrowseWorkoutDetailAdapter(getContext(),workouts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onWorkoutClick(int position) {
        //Toast.makeText(getContext(), "Clicked: " + workouts.get(position).getName(), Toast.LENGTH_SHORT).show();
        Log.i("BrowsePublicFragment", "A workout has been clicked: " + workouts.get(position).getName());
        //Intent intent = new Intent(this, XXX.java); //TODO Display workout activity here
        //intent.putExtra("workout", workout);
        //startActivity(intent);
    }
}