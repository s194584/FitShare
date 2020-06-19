package com.example.youfit;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Workout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BrowseWorkoutDetailAdapter extends RecyclerView.Adapter<BrowseWorkoutDetailAdapter.ViewHolder> {

    private static final String TAG = "BrowseWorkoutDetailAdap";

    private Context mContext;
    private List<Workout> mWorkouts;
    private OnWorkoutListener mOnWorkoutListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, com.example.youfit.ViewHolder {
        TextView workoutName;
        TextView workoutType;
        TextView workoutTime;
        TextView workoutDifficulty;
        Button startWorkOutButton;
        RelativeLayout detailLayout;
        OnWorkoutListener onWorkoutListener;

        public ViewHolder(@NonNull View itemView, final OnWorkoutListener onWorkoutListener) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workout_info_text);
            workoutType = itemView.findViewById(R.id.workout_type);
            workoutTime = itemView.findViewById(R.id.workout_time);
            workoutDifficulty = itemView.findViewById(R.id.workout_difficulty);
            startWorkOutButton = itemView.findViewById(R.id.startworkoutfrombrowser_button);
            this.onWorkoutListener = onWorkoutListener;
            detailLayout = itemView.findViewById(R.id.browse_workout_layout);

            itemView.setOnClickListener(this);
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

    public BrowseWorkoutDetailAdapter(Context mContext, List<Workout> workouts, OnWorkoutListener onWorkoutListener)
    {
        mWorkouts = workouts;
        this.mContext = mContext;
        this.mOnWorkoutListener = onWorkoutListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_workoutlist,parent,false);
        ViewHolder holder = new ViewHolder(view, mOnWorkoutListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String workoutName = "Name: " + mWorkouts.get(position).getName();
        String workoutType = "Type: " + mWorkouts.get(position).formatType();
        String workoutTime = "Time: " + mWorkouts.get(position).TimeAsString();
        String workoutDifficulty = "Difficulty: " + mWorkouts.get(position).getWorkoutDifficulty();
        holder.workoutName.setText(workoutName);
        holder.workoutType.setText(workoutType);
        holder.workoutTime.setText(workoutTime);
        holder.workoutDifficulty.setText(workoutDifficulty);
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    public interface OnWorkoutListener
    {
        void onWorkoutClick(int Position);
        void onButtonClick(int Position);

    }

}
