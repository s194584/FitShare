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
    private OnExerciseListener onExerciseListener;

    public interface OnExerciseListener{
        void onExerciseClicked(int position);
    }

    // Creating a holder for the views used in RecyclerView
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ExerciseDetailCardBinding mBinding;
        private OnExerciseListener exerciseListener;
        public ExerciseViewHolder(ExerciseDetailCardBinding binding, OnExerciseListener exerciseListener) {
            super(binding.getRoot());
            mBinding = binding;
            this.exerciseListener = exerciseListener;

            mBinding.getRoot().setOnClickListener(this);
        }

        public void bind(Exercise exercise){
            mBinding.setExercise(exercise);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            exerciseListener.onExerciseClicked(getAdapterPosition());
        }
    }

    public ExersiceAdapter(List<Exercise> exercises,OnExerciseListener onExerciseListener) {
        this.exercises = exercises;
        this.onExerciseListener = onExerciseListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        ExerciseDetailCardBinding edcb = ExerciseDetailCardBinding.inflate(li,parent,false);

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(edcb,onExerciseListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ((ExerciseViewHolder) holder).bind(exercises.get(position));

        ((ExerciseViewHolder) holder).mBinding.getRoot().findViewById(R.id.cancelExerciseImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                exercises.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos,getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }



}
