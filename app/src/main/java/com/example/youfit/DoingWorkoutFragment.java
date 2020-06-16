package com.example.youfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class DoingWorkoutFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_doingworkout, container, false);

        setWaterAmountText = view.findViewById(R.id.setWaterAmount_text);
        setWaterAmountText.setText(""+setWaterAmount);
        waterAmountText = view.findViewById(R.id.waterAmount_text);
        waterAmountText.setText(""+totalWaterAmount + " mL / 2000 mL");
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
                setWaterAmountText.setText(""+setWaterAmount + " mL");
                waterAmountText.setText(""+totalWaterAmount + " mL / 2000 mL");
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