package com.example.youfit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseElement;
import com.example.youfit.domain.ExerciseType;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;
import java.util.Collections;

public class WorkoutFragment extends Fragment implements EditExerciseDialogFragment.EditExerciseDialogFragmentListener, ExersiceAdapter.OnExerciseListener{
    private final String TAG = "WorkoutFragment";

    EditText nameEditText;

    // New recycle view
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public ArrayList<Exercise> exercises = new ArrayList<>();
    protected Workout currentWorkout;
    String existingWorkoutKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.currentWorkout = (Workout) getArguments().get("newWorkout");
            existingWorkoutKey = getArguments().getString("key");
            exercises = currentWorkout.getExercises();
            Log.i(TAG, "onCreate: got key: " + getArguments().getString("key"));
        } else{
            currentWorkout = new Workout();
            existingWorkoutKey = "";
            Log.d(TAG, "got empty key");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEditText = view.findViewById(R.id.edittext_workout_name);
        nameEditText.setText(currentWorkout.getName());

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.list_exercise);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ExersiceAdapter(exercises,this);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int holderPos = viewHolder.getAdapterPosition();
                int targetPos = target.getAdapterPosition();

                Collections.swap(exercises,holderPos,targetPos);
                Log.i(TAG,"ITEM MOVED! Holder: "+holderPos+" Target: "+targetPos);
                mAdapter.notifyItemMoved(holderPos,targetPos);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Do nothing
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        getActivity().findViewById(R.id.addPauseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exercise pause = new Exercise(new ExerciseElement(getString(R.string.pause), ExerciseType.PAUSE.name(),getString(R.string.pause_description)));
                showEditExercise(pause,-1);
            }
        });

        // Go to Workout Settings
        getActivity().findViewById(R.id.button_workout_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(),
                            R.string.workoutname_required,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nameEditText.getText().toString().length() > 20)
                {
                    Toast.makeText(view.getContext(), R.string.workoutname_to_short, Toast.LENGTH_LONG).show();
                    return;
                }
                if (exercises.isEmpty()) {
                    Toast.makeText(view.getContext(), R.string.workout_excersize_required, Toast.LENGTH_LONG).show();
                    return;
                }


                currentWorkout.setName(nameEditText.getText().toString());
                currentWorkout.setExercises(exercises);
                Log.i("EXERCISEFRAGMENT", "WORKOUT CREATED");
                Bundle bundle = new Bundle();
                bundle.putParcelable("newWorkout", currentWorkout);
                bundle.putString("key", existingWorkoutKey);
                NavHostFragment.findNavController(WorkoutFragment.this).navigate(R.id.action_workoutFragment_to_workoutSettingsFragment, bundle);
            }
        });

        Button addExerciseBtn = view.findViewById(R.id.addExerciseButton);
        addExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditExercise(new Exercise(new ExerciseElement()),-1);
            }
        });
    }

    public void showEditExercise(Exercise exercise, int position){
        EditExerciseDialogFragment dialog = new EditExerciseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("editExercise",exercise);
        bundle.putInt("position",position);
        dialog.setTargetFragment(WorkoutFragment.this,1);
        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(),"EditExerciseDialogFragment");
    }

    @Override
    public void onDialogSave(Exercise exercise, int position) {
        if(position == -1){
            exercises.add(exercise);
            mAdapter.notifyItemInserted(exercises.size()-1);
        } else {
            Log.i(TAG,"Should change exercise.");
            mAdapter.notifyItemChanged(position);
        }

    }

    @Override
    public void onExerciseClicked(int position) {
        showEditExercise(exercises.get(position),position);
    }
}
