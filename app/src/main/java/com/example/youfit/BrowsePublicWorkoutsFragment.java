package com.example.youfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_browse_public_workouts, container, false);

        Log.i("BrowsePrivateFragment", "2: Creating example data");
        // Load workouts from server
        Server server = ((MainActivity) getActivity()).getServer();
        workouts = server.getPublicWorkouts();


        initRecyclerView(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.publicWorkoutsRV);
        BrowseWorkoutDetailAdapter adapter = new BrowseWorkoutDetailAdapter(getContext(), workouts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onStop() {
        super.onStop();
        //workouts.clear();
    }

    public void onWorkoutClick(int position) {
        Toast.makeText(getContext(), "Clicked: " + workouts.get(position).getName(), Toast.LENGTH_SHORT).show();
        Log.i("BrowsePublicFragment", "A workout has been clicked: " + workouts.get(position).getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", workouts.get(position));
        NavHostFragment.findNavController(BrowsePublicWorkoutsFragment.this)
                .navigate(R.id.action_browseWorkoutsFragment_to_viewWorkoutDetailsFragment, bundle);
    }

    @Override
    public void onButtonClick(int position) {
        Toast.makeText(getContext(), "BUTTON: " + workouts.get(position).getName(), Toast.LENGTH_SHORT).show();
        Log.i("BrowsePublicFragment", "A workout has been clicked: " + workouts.get(position).getName());
    }
}