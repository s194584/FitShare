package com.example.youfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;
import com.example.youfit.domain.Workout;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class WorkoutFragment extends Fragment implements EditExerciseDialogFragment.EditExerciseDialogFragmentListener, ExersiceAdapter.OnExerciseListener{
    private final String TAG = "WorkoutFragment";


    private String workoutSelected;
    private String exerciseSelected;

    EditText nameEditText;
    // New recycle view
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public ArrayList<Exercise> exercises = new ArrayList<>();
    protected Workout currentWorkout;

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
            exercises = currentWorkout.getExercises();
        } else{
            currentWorkout = new Workout();
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

        getActivity().findViewById(R.id.addPauseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exercise pause = new Exercise("Pause");
                pause.setType(ExerciseType.TIME.name());
                showEditExercise(pause,-1);
            }
        });

        // Go to Workout Settings
        getActivity().findViewById(R.id.button_workout_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(),
                            "Please enter a name for your workout",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                currentWorkout.setName(nameEditText.getText().toString());
                currentWorkout.setExercises(exercises);
                Log.i("EXERCISEFRAGMENT", "WORKOUT CREATED");
                Bundle bundle = new Bundle();
                bundle.putParcelable("newWorkout", currentWorkout);
                NavHostController navController = (NavHostController) NavHostFragment.findNavController(WorkoutFragment.this);
                navController.navigate(R.id.action_workoutFragment_to_workoutSettingsFragment, bundle);
                navController.popBackStack(R.id.workoutSettingsFragment,false);
//                navController.popBackStack(R.id.workoutFragment,false);

            }
        });

        Button addExcersizeBtn = view.findViewById(R.id.addExerciseBtn);
        addExcersizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditExercise(new Exercise(""),-1);
            }
        });
//        addExcersizeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEditExercise();
//
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Add exercise");
//                // inflate view
//                View viewInflated = LayoutInflater.from(getContext()).
//                        inflate(R.layout.popup_input_exercise,
//                                (ViewGroup) getView(), false);
//                // Set up the input
//                final AutoCompleteTextView inputExercise = viewInflated.findViewById(R.id.inputExcersize);
//
//                //set up autocomplete adapter
//                ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(getContext(),
//                        R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.rep_exercises));
//                inputExercise.setAdapter(autocompleteAdapter);
//
//
//                builder.setView(viewInflated);
//
//                // Set up the buttons
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//
//                        exerciseSelected = inputExercise.getText().toString();
//
////                        if (exerciseStrings.get(ExerciseType.REPETITION).contains(exerciseSelected)) {
////                            //make excersize and add it to the currentWorkout.
////                            Exercise exercise = new Exercise(exerciseSelected,ExerciseType.REPETITION);
////                            exercises.add(exercise);
////
////                        } else if (exerciseStrings.get(ExerciseType.TIME).contains(exerciseSelected)) {
////                            Exercise exercise = new Exercise(exerciseSelected,ExerciseType.TIME);
////                            exercises.add(exercise);
////                        } else {
////                            Toast.makeText(getContext(),
////                                    "Please enter a vaild workout form",
////                                    Toast.LENGTH_SHORT).show();
////                        }
//                        exercises.add(new Exercise(exerciseSelected));
//                        mAdapter.notifyItemInserted(exercises.size()-1);
//                    }
//                });
//                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.show();
//            }
//        });
    }

    public void showEditExercise(Exercise exercise,int position){
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
