package com.example.youfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Workout;

import java.util.List;

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.ViewHolder>
{

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView workoutName;
        public TextView workoutTime;
        public ImageButton startWorkout;
        OnWorkoutListener onWorkoutListener;

        public ViewHolder(View itemView, final OnWorkoutListener onWorkoutListener)
        {
            super(itemView);
            workoutName = (TextView) itemView.findViewById(R.id.workoutNameText);
            workoutTime = (TextView) itemView.findViewById(R.id.workoutTimeText);
            startWorkout = (ImageButton) itemView.findViewById(R.id.startWorkoutButton);
            startWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mOnWorkoutListener.onButtonClick(getAdapterPosition());
                }
            });

            this.onWorkoutListener = onWorkoutListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onWorkoutListener.onWorkoutClick(getAdapterPosition());
        }
    }

    private List<Workout> mWorkouts;
    private OnWorkoutListener mOnWorkoutListener;
    private int weekOfDay;

    public WorkoutDetailAdapter(List<Workout> workouts, OnWorkoutListener onWorkoutListener)
    {
        mWorkouts = workouts;
        this.mOnWorkoutListener = onWorkoutListener;
    }

    @Override
    public WorkoutDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View workoutDetailView = inflater.inflate(R.layout.item_workout_detail, parent, false);

        return new ViewHolder(workoutDetailView, mOnWorkoutListener);
    }

    @Override
    public void onBindViewHolder(WorkoutDetailAdapter.ViewHolder viewHolder, int position)
    {
        Workout workout = mWorkouts.get(position);
        TextView textView = viewHolder.workoutName;
        textView.setText(workout.getName());
        TextView textView1 = viewHolder.workoutTime;
        textView1.setText(""+ workout.timeAsString());
    }

    @Override
    public int getItemCount()
    {
        return mWorkouts.size();
    }

}
