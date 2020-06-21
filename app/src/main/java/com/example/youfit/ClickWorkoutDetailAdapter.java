package com.example.youfit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;

import java.util.List;

public class ClickWorkoutDetailAdapter extends RecyclerView.Adapter<ClickWorkoutDetailAdapter.ViewHolder> {

    private static final String TAG = "ClickWorkoutDetailAdap";

    private Context mContext;
    private List<Exercise> mExercises;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        TextView exerciseRepOrTime;
        TextView timeOrReps;
        RelativeLayout detailLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.excersizeNameTextView);
            exerciseRepOrTime = itemView.findViewById(R.id.repTimeTextView);
            timeOrReps = itemView.findViewById(R.id.amoutnTextView);
            detailLayout = itemView.findViewById(R.id.view_exercises_layout);

        }
    }

    public ClickWorkoutDetailAdapter(Context mContext, List<Exercise> exercises)
    {
        mExercises = exercises;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exerciselist,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
