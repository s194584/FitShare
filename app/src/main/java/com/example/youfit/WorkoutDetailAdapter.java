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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView workoutName;
        public TextView workoutTime;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener)
        {
            super(itemView);
            workoutName = (TextView) itemView.findViewById(R.id.workoutNameText);
            workoutTime = (TextView) itemView.findViewById(R.id.workoutTimeText);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    private List<Workout> mWorkouts;
    private OnNoteListener mOnNoteListener;

    public WorkoutDetailAdapter(List<Workout> workouts, OnNoteListener onNoteListener)
    {
        mWorkouts = workouts;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public WorkoutDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View workoutDetailView = inflater.inflate(R.layout.item_workout_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(workoutDetailView, mOnNoteListener);
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

    public interface OnNoteListener
    {
        void onNoteClick(int Position);
    }
}
