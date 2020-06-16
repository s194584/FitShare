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
import androidx.recyclerview.widget.RecyclerView;

import com.example.youfit.domain.Workout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BrowseWorkoutDetailAdapter extends RecyclerView.Adapter<BrowseWorkoutDetailAdapter.ViewHolder> {

    private static final String TAG = "BrowseWorkoutDetailAdap";

    private Context mContext;
    private List<Workout> mWorkouts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName;
        TextView workoutType;
        TextView workoutTime;
        TextView workoutDifficulty;
        Button startWorkOutButton;
        RelativeLayout detailLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workout_info_text);
            workoutType = itemView.findViewById(R.id.workout_type);
            workoutTime = itemView.findViewById(R.id.workout_time);
            workoutDifficulty = itemView.findViewById(R.id.workout_difficulty);
            startWorkOutButton = itemView.findViewById(R.id.startworkoutfrombrowser_button);
            detailLayout = itemView.findViewById(R.id.browse_workout_layout);
        }
    }

    public BrowseWorkoutDetailAdapter(Context mContext, List<Workout> workouts)
    {
        mWorkouts = workouts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_workoutlist,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String workoutName = "Name: " + mWorkouts.get(position).getName();
        String workoutType = "Type: " + mWorkouts.get(position).getType();
        String workoutTime = "Time: " + mWorkouts.get(position).getTimeString();
        String workoutDifficulty = "Difficulty: Medium";
        holder.workoutName.setText(workoutName);
        holder.workoutType.setText(workoutType);
        holder.workoutTime.setText(workoutTime);
        holder.workoutDifficulty.setText(workoutDifficulty);

        holder.detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "test!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.startWorkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Button!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }


}
