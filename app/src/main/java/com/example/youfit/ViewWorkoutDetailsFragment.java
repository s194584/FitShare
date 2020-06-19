package com.example.youfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;

public class ViewWorkoutDetailsFragment extends Fragment {

    private static final String workoutString = "WORKOUT";
    private static final String TAG = "ViewWorkoutDetailsFrag";

    private TextView workoutName;
    private TextView workoutDifficulty;
    private TextView workoutType;
    private TextView workoutTime;
    private TextView workoutCreator;
    private TextView workoutDescription;
    private Button startWorkoutButton;
    private Button deleteWorkoutButton;
    private Button editWorkoutButton;
    private boolean fromPublic;

    private Workout mWorkout;
    ArrayList<Exercise> exercises = new ArrayList<Exercise>();


    public ViewWorkoutDetailsFragment() {
        // Required empty public constructor
    }

    public static ViewWorkoutDetailsFragment newInstance(String param1) {
        ViewWorkoutDetailsFragment fragment = new ViewWorkoutDetailsFragment();
        Bundle args = new Bundle();
        args.putString(workoutString, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorkout = getArguments().getParcelable(workoutString);
            fromPublic = getArguments().getBoolean("public");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_workout_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formatAssets(view);

        if (fromPublic) {
            deleteWorkoutButton.setVisibility(View.GONE);
            editWorkoutButton.setVisibility(View.GONE);
        }

        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DoWorkoutActivity.class);
                intent.putExtra("workout", mWorkout);
                startActivity(intent);
            }
        });

        deleteWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete workout " + mWorkout.getName() + "?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deletion from database
                                Server server = ((MainActivity) getActivity()).getServer();
                                server.removeWorkout(mWorkout);

                                // go to previous fragment
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }).setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        editWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ViewWorkoutDetails", "A workout has been clicked");
                Bundle bundle = new Bundle();
                bundle.putParcelable("newWorkout", mWorkout);
                NavHostFragment.findNavController(ViewWorkoutDetailsFragment.this)
                        .navigate(R.id.action_viewWorkoutDetailsFragment_to_workoutFragment, bundle);
            }
        });

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.exercisedetailsRV);
        ClickWorkoutDetailAdapter adapter = new ClickWorkoutDetailAdapter(getContext(), exercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void formatAssets(View view) {
        startWorkoutButton = view.findViewById(R.id.start_workout_button);
        deleteWorkoutButton = view.findViewById(R.id.delete_workout_button);
        editWorkoutButton = view.findViewById(R.id.edit_workout_button);

        workoutName = view.findViewById(R.id.workout_name_view);
        workoutDifficulty = view.findViewById(R.id.workout_difficulty_view);
        workoutType = view.findViewById(R.id.workout_type_view);
        workoutTime = view.findViewById(R.id.workout_time_view);
        workoutCreator = view.findViewById(R.id.workout_creator_view);
        workoutDescription = view.findViewById(R.id.workout_description);

        String workoutNameString = "Name: " + mWorkout.getName();
        String workoutDifficultyString = "Difficulty: " + mWorkout.formatDifficulty();
        String workoutTypeString = "Type: " + mWorkout.formatType();
        String workoutTimeString = "Estimated time: " + mWorkout.TimeAsString();
        String workoutCreatorString = "Creator: the_big_guy/girl";
        String workoutDescriptionString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                "        sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex\n" +
                "        ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse\n" +
                "        cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident,\n" +
                "        sunt in culpa qui officia deserunt mollit anim id est laborum." + mWorkout.getDescription();

        workoutName.setText(workoutNameString);
        workoutDifficulty.setText(workoutDifficultyString);
        workoutType.setText(workoutTypeString);
        workoutTime.setText(workoutTimeString);
        workoutCreator.setText(workoutCreatorString);
        workoutDescription.setText(workoutDescriptionString);

        workoutDescription.setMovementMethod(new ScrollingMovementMethod());

        exercises = mWorkout.getExercises();
    }
}