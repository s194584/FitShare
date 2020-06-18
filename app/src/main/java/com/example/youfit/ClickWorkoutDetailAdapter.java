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

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.Workout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickWorkoutDetailAdapter extends RecyclerView.Adapter<ClickWorkoutDetailAdapter.ViewHolder> {

    private static final String TAG = "ClickWorkoutDetailAdap";

    private Context mContext;
    private List<Exercise> mExercises;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        RelativeLayout detailLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exercise_name);
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

        String exerciseName = mExercises.get(position).getName();
        holder.exerciseName.setText(exerciseName);
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }


    public String formatType(String type) {
        if (type != null) {
            String firstLetter = type.substring(0,1);
            String rest = type.substring(1);
            return firstLetter.toUpperCase() + rest.toLowerCase();
        }
        return null;
    }

}
