package com.example.youfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Exercise;

import java.util.List;

public class ExersiceAdapter extends RecyclerView.Adapter {
    private List<Exercise> exercises;

    // Creating a holder for the views used in RecyclerView
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout layout;
        public ExerciseViewHolder(View itemView) {
            super(itemView);
            layout = (ConstraintLayout) itemView;
        }
    }

    public ExersiceAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_detail_card,parent,false);

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView name =(TextView) ((ExerciseViewHolder) holder).layout.getViewById(R.id.excersizeNameTextView);
        name.setText(exercises.get(position).getName());
        TextView rep =(TextView) ((ExerciseViewHolder) holder).layout.getViewById(R.id.amoutnTextView);
        rep.setText(""+exercises.get(position).getReps());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}
