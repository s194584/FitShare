package com.example.youfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class StatisticsFragment extends Fragment {

    int waterGoal = 2000;
    int setWaterAmount = 0;
    TextView setWaterAmountText;
    int totalWaterAmount = 0; //TODO firebase, baby
    TextView waterAmountText;
    androidx.appcompat.widget.AppCompatImageButton addWaterButton;
    androidx.appcompat.widget.AppCompatImageButton subtractWaterButton;
    androidx.appcompat.widget.AppCompatImageButton drinkButton;

    int totalWorkoutsWeekly = 0; //TODO firebase, baby
    TextView totalWorkoutsWeeklyText;
    int totalTimeWeekly = 0; //TODO firebase, baby
    TextView totalTimeWeeklyText;

    int totalWorkoutsTotal = 0; //TODO firebase, baby
    TextView totalWorkoutsTotalText;
    int totalTimeTotal = 0; //TODO firebase, baby
    TextView totalTimeTotalText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        //TODO implement with firebase data and uncomment
        /*
        int totalWorkoutTimeToday = 0;
        for(Workout x : user.getDailyWorkouts())
        {
            totalWorkoutTimeToday += x.getTime();
        }
        waterGoal += 200*(totalWorkoutTimeToday/15);
        */

        setWaterAmountText = view.findViewById(R.id.setWaterAmount_text);
        setWaterAmountText.setText(""+setWaterAmount);
        waterAmountText = view.findViewById(R.id.waterAmount_text);
        waterAmountText.setText(""+totalWaterAmount + " mL / " + waterGoal + " mL");
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
                //TODO Save values in firebase
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
                            "Congratulations, you've reached your water goal! Good job! you are awesome!",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        totalWorkoutsWeeklyText = view.findViewById(R.id.total_workouts_weekly_text);
        totalWorkoutsWeeklyText.setText(""+totalWorkoutsWeekly);
        totalTimeWeeklyText = view.findViewById(R.id.total_workouts_time_weekly_text);
        totalTimeWeeklyText.setText(""+totalTimeWeekly);

        totalWorkoutsTotalText = view.findViewById(R.id.total_workouts_total_text);
        totalWorkoutsTotalText.setText(""+totalWorkoutsTotal);
        totalTimeTotalText = view.findViewById(R.id.total_workouts_time_total_text);
        totalTimeTotalText.setText(""+totalTimeTotal);



        // Inflate the layout for this fragment
        return view;
    }
}