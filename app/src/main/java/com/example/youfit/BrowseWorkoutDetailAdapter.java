package com.example.youfit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Workout;

import java.util.ArrayList;
import java.util.List;

public class BrowseWorkoutDetailAdapter extends RecyclerView.Adapter<BrowseWorkoutDetailAdapter.BrowseViewHolder> {

    private static final String TAG = "BrowseWorkoutDetailAdap";

    private List<Workout> mWorkouts;
    private List<Workout> mFilteredWorkouts;
    private OnWorkoutListener mOnWorkoutListener;


    public static class BrowseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, com.example.youfit.ViewHolder {
        private TextView workoutName;
        private TextView workoutType;
        private TextView workoutTime;
        private TextView workoutDifficulty;
        private OnWorkoutListener onWorkoutListener;

        public BrowseViewHolder(@NonNull View itemView, final OnWorkoutListener onWorkoutListener) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workout_info_text);
            workoutType = itemView.findViewById(R.id.workout_type);
            workoutTime = itemView.findViewById(R.id.workout_time);
            workoutDifficulty = itemView.findViewById(R.id.workout_difficulty);
            this.onWorkoutListener = onWorkoutListener;
            itemView.setOnClickListener(this);

            Button startWorkOutButton = itemView.findViewById(R.id.startworkoutfrombrowser_button);
            startWorkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWorkoutListener.onButtonClick(getAdapterPosition());
                }
            });

        }

        @Override
        public void onClick(View v) {
            onWorkoutListener.onWorkoutClick(getAdapterPosition());
        }
    }

    public BrowseWorkoutDetailAdapter(List<Workout> workouts, OnWorkoutListener onWorkoutListener)
    {
        mWorkouts = workouts;
        mFilteredWorkouts = mWorkouts;
        this.mOnWorkoutListener = onWorkoutListener;
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_workoutlist,parent,false);
        return new BrowseViewHolder(view, mOnWorkoutListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String workoutName = "Name: " + mFilteredWorkouts.get(position).getName();
        String workoutType = "Type: " + Utility.formatEnum(mFilteredWorkouts.get(position).getWorkoutType());
        String workoutTime = "Time: " + mFilteredWorkouts.get(position).timeAsString();
        String workoutDifficulty = "Difficulty: " + Utility.formatEnum(mFilteredWorkouts.get(position).getWorkoutDifficulty());

        holder.workoutName.setText(workoutName);
        holder.workoutType.setText(workoutType);
        holder.workoutTime.setText(workoutTime);
        holder.workoutDifficulty.setText(workoutDifficulty);
    }

    @Override
    public int getItemCount() {
        return mFilteredWorkouts.size();
    }

    public void filter(String difficultyFilter, String typeFilter) {

        Log.i(TAG, "diff: "+difficultyFilter+" type: "+typeFilter);
        mFilteredWorkouts = new ArrayList<>();
        // Four strategies
        if(difficultyFilter.equals("Difficulty filter")&&typeFilter.equals("Type filter")){
            mFilteredWorkouts = mWorkouts;
        } else if(!typeFilter.equals("Type filter") && difficultyFilter.equals("Difficulty filter")){
            for (Workout workout : mWorkouts) {
                if (workout.getWorkoutType().toLowerCase().contains(typeFilter.toLowerCase())) {
                    mFilteredWorkouts.add(workout);
                }
            }
        } else if(typeFilter.equals("Type filter")){
            for (Workout workout : mWorkouts) {
                if (workout.getWorkoutDifficulty().toLowerCase().contains(difficultyFilter.toLowerCase())) {
                    mFilteredWorkouts.add(workout);
                }
            }
        }else {
            for (Workout workout : mWorkouts){
                if(workout.getWorkoutDifficulty().toLowerCase().contains(difficultyFilter.toLowerCase())&&
                        workout.getWorkoutType().toLowerCase().contains(typeFilter.toLowerCase())){
                    mFilteredWorkouts.add(workout);
                }
            }
        }

        notifyDataSetChanged();
    }

}
