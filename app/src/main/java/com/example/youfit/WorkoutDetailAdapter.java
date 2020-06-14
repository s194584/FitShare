package com.example.youfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Workout;

import java.util.List;

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.ViewHolder>
{
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView workoutName;
        public TextView workoutTime;

        public ViewHolder(View itemView)
        {
            super(itemView);
            workoutName = (TextView) itemView.findViewById(R.id.workoutNameText);
            workoutTime = (TextView) itemView.findViewById(R.id.workoutTimeText);


        }
    }

    private List<Workout> mWorkouts;

    public WorkoutDetailAdapter(List<Workout> workouts)
    {
        mWorkouts = workouts;
    }

    @Override
    public WorkoutDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View workoutDetailView = inflater.inflate(R.layout.item_workout_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(workoutDetailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WorkoutDetailAdapter.ViewHolder viewHolder, int position)
    {
        Workout workout = mWorkouts.get(position);

        TextView textView = viewHolder.workoutName;
        textView.setText(workout.getName());
        TextView textView1 = viewHolder.workoutTime;
        textView1.setText(""+workout.getTime());

    }

    @Override
    public int getItemCount()
    {
        return mWorkouts.size();
    }
}