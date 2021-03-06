package com.example.youfit;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.Statistics;
import com.google.firebase.database.DataSnapshot;


public class StatisticsFragment extends Fragment implements DatabaseListener {

    private final String TAG = "StatisticsFragment";

    int waterGoal = 2000;
    int setWaterAmount = 0;
    TextView setWaterAmountText;
    int totalWaterAmount = 0;
    TextView waterAmountText;
    androidx.appcompat.widget.AppCompatImageButton addWaterButton;
    androidx.appcompat.widget.AppCompatImageButton subtractWaterButton;
    androidx.appcompat.widget.AppCompatImageButton drinkButton;

    int totalWorkoutsWeekly = 0;
    TextView totalWorkoutsWeeklyText;
    long totalTimeWeekly = 0;
    TextView totalTimeWeeklyText;

    int totalWorkoutsTotal = 0;
    TextView totalWorkoutsTotalText;
    long totalTimeTotal = 0;
    TextView totalTimeTotalText;

    private View view;
    private Statistics stats;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_statistics, container, false);

        ((MainActivity)getActivity()).getServer().loadUserStats(this);
        return view;
    }

    private void init(View view) {
        totalWaterAmount = stats.getTotalWater();
        totalWorkoutsTotal = stats.getTotalWorkoutsTotal();
        totalTimeTotal = stats.getTotalTimeTotal();
        totalTimeWeekly = stats.getWeeklyTimeTotal();
        totalWorkoutsWeekly = stats.getWeeklyWorkoutsTotal();

        setWaterAmountText = view.findViewById(R.id.setWaterAmount_text);
        setWaterAmountText.setText(""+setWaterAmount);
        waterAmountText = view.findViewById(R.id.waterAmount_text);
        waterAmountText.setText(""+stats.getTotalWater() + " mL / " + waterGoal + " mL");
        addWaterButton = view.findViewById(R.id.addWaterButton);
        addWaterButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWaterAmount += 100;
                setWaterAmountText.setText(""+setWaterAmount + " mL");
            }
        });

        subtractWaterButton = view.findViewById(R.id.subtractWaterButton);
        subtractWaterButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setWaterAmount -= 100;
                setWaterAmountText.setText(""+setWaterAmount + " mL");
            }
        });

        drinkButton = view.findViewById(R.id.drinkWaterButton);
        drinkButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                totalWaterAmount += setWaterAmount;
                setWaterAmount = 0;
                if(totalWaterAmount < 0)
                {
                    totalWaterAmount = 0;
                }
                setWaterAmountText.setText(""+setWaterAmount + " mL");
                waterAmountText.setText(""+totalWaterAmount + " mL / " + waterGoal + " mL");
                if (totalWaterAmount >= 2000)
                {
                    Toast toast = Toast.makeText(getActivity(),
                            R.string.watergoal_reached,
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        view.findViewById(R.id.button_reset_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalWaterAmount = 0;
                waterAmountText.setText(""+totalWaterAmount + " mL / " + waterGoal + " mL");
            }
        });
        //Find and set weekly
        totalWorkoutsWeeklyText = view.findViewById(R.id.total_workouts_weekly_text);
        totalWorkoutsWeeklyText.setText(""+totalWorkoutsWeekly);
        totalTimeWeeklyText = view.findViewById(R.id.total_workouts_time_weekly_text);
        totalTimeWeeklyText.setText(Utility.millisToHour(totalTimeWeekly));

        //Find and set total
        totalWorkoutsTotalText = view.findViewById(R.id.total_workouts_total_text);
        totalWorkoutsTotalText.setText(""+totalWorkoutsTotal);
        totalTimeTotalText = view.findViewById(R.id.total_workouts_time_total_text);
        totalTimeTotalText.setText(Utility.millisToHour(totalTimeTotal));
    }

    @Override
    public void onStop() {
        super.onStop();
        stats.setTotalWater(totalWaterAmount);
        ((MainActivity)getActivity()).getServer().changeStats(stats);
    }

    @Override
    public void onComplete(DataSnapshot dataSnapshot) {
        Log.i(TAG, "onComplete: loaded stat");
        stats = dataSnapshot.getValue(Statistics.class);
        init(view);
    }
}