package com.example.youfit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Exercise;

import java.util.List;

public class ClickWorkoutDetailAdapter extends RecyclerView.Adapter<ClickWorkoutDetailAdapter.DetailsViewHolder> {

    private static final String TAG = "ClickWorkoutDetailAdap";

    private List<Exercise> mExercises;

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        TextView exerciseRepOrTime;
        TextView timeOrReps;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.excersizeNameTextView);
            exerciseRepOrTime = itemView.findViewById(R.id.repTimeTextView);
            timeOrReps = itemView.findViewById(R.id.amoutnTextView);
        }
    }

    public ClickWorkoutDetailAdapter(List<Exercise> exercises)
    {
        mExercises = exercises;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exerciselist,parent,false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String exerciseName = mExercises.get(position).retrieveName();
        String exerciseRepsOrTime = mExercises.get(position).repsOrTime();
        String amount = mExercises.get(position).retrieveFormattedAmountString();
        holder.exerciseName.setText(exerciseName);
        holder.exerciseRepOrTime.setText(exerciseRepsOrTime);
        holder.timeOrReps.setText(amount);
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

}
