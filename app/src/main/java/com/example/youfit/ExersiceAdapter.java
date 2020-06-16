package com.example.youfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.databinding.ExerciseDetailCardBinding;
import com.example.youfit.domain.Exercise;

import java.util.List;

public class ExersiceAdapter extends RecyclerView.Adapter {
    private final String TAG = "ExersiceAdapter";

    private List<Exercise> exercises;

    // Creating a holder for the views used in RecyclerView
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        private ExerciseDetailCardBinding mBinding;

        public ExerciseViewHolder(ExerciseDetailCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Exercise exercise){
            mBinding.setExercise(exercise);
            mBinding.executePendingBindings();
        }
    }

    public ExersiceAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        ExerciseDetailCardBinding edcb = ExerciseDetailCardBinding.inflate(li,parent,false);

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(edcb);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ExerciseViewHolder) holder).bind(exercises.get(position));
        ((ExerciseViewHolder) holder).mBinding.getRoot().findViewById(R.id.cancelExerciseImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercises.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}
