package com.example.youfit;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseType;
import com.example.youfit.domain.Workout;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;

public class DoWorkoutActivity extends AppCompatActivity{

    private ViewFlipper mFlipper;
    private TextView mText1, mText2, mTextExerciseType1, mTextExerciseType2;
    private int mCount, mCurrentLayoutState;

    private SeekBar seekBar;

    private GestureDetector mGesture;

    private CountDownTimer timer;
    private long timeLeftInMiliseconds;
    private boolean timeRunning = false;
    private Button timerButton;

    private String temp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        Intent intent = getIntent();
        Workout workout = intent.getParcelableExtra("workout");

        timerButton = findViewById(R.id.timerButton);
        timerButton.setVisibility(View.INVISIBLE);

        final ArrayList<Exercise> exercises = workout.getExercises();

        // Determines which layout to display
        mCurrentLayoutState = 0;

        // Determines which exercise, we have reached
        mCount = 0;

        // Initialization of stuff
        mFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        mText1 = (TextView) findViewById(R.id.textView1);
        mText2 = (TextView) findViewById(R.id.textView3);
        mTextExerciseType1 = (TextView) findViewById(R.id.textViewExerciseType1);
        mTextExerciseType2 = (TextView) findViewById(R.id.textViewExerciseType2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setMax(exercises.size());

        mText1.setText(exercises.get(0).getName());

        if (isTime(exercises.get(0))){
            timeLeftInMiliseconds = exercises.get(0).getTime();
            showTimedExercise(mTextExerciseType1);
        } else {
            temp = exercises.get(0).getReps() + "";
            mTextExerciseType1.setText(temp);
        }



        // Gesture detector detects the swipe motion
        mGesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityX < -10.0f && mCount < exercises.size()-1 && mCount >= 0) { // Swipe right
                        Log.i("DoWorkoutActivity", "Swipe right");
                        mCount++;

                        seekBar.setProgress(mCount);

                        // if mCurrentLayoutState == 0, then mCurrentLayoutState is set to 1, otherwise 0.
                        mCurrentLayoutState = mCurrentLayoutState == 0 ? 1 : 0;
                        System.out.println(mCurrentLayoutState);
                        System.out.println("mCount = " + mCount);

                        switchLayoutStateTo(mCurrentLayoutState, exercises.get(mCount), "Swipe right");

                    } else if (velocityX > 10.0f && mCount > 0) { // Swipe left
                        Log.i("DoWorkoutActivity", "Swipe left");

                        mCount--;

                        seekBar.setProgress(mCount);

                        mCurrentLayoutState = mCurrentLayoutState == 0 ? 1 : 0;
                        System.out.println(mCurrentLayoutState);
                        System.out.println("mCount = " + mCount);

                        switchLayoutStateTo(mCurrentLayoutState, exercises.get(mCount), "Swipe left");

                    }
                return true;
            }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    public void switchLayoutStateTo(int switchTo, Exercise exercise, String swipeDirection) {

        if (swipeDirection.equals("Swipe left")){

            // Animation
            mFlipper.setInAnimation(inFromLeftAnimation());
            mFlipper.setOutAnimation(outToRightAnimation());
            mFlipper.showPrevious();

            if (switchTo == 0){
                mText1.setText(exercise.getName());

                if (isTime(exercise)){
                    timerButton.setVisibility(View.VISIBLE);
                    timeLeftInMiliseconds = exercise.getTime();
                    showTimedExercise(mTextExerciseType1);

                } else {
                    timerButton.setVisibility(View.INVISIBLE);
                    temp = exercise.getReps() + "";
                    mTextExerciseType1.setText(temp);
                }

            }
            else {
                mText2.setText(exercise.getName());

                if (isTime(exercise)){
                    timerButton.setVisibility(View.VISIBLE);
                    timeLeftInMiliseconds = exercise.getTime();
                    showTimedExercise(mTextExerciseType2);
                } else {
                    timerButton.setVisibility(View.INVISIBLE);
                    temp = exercise.getReps() + "";
                    mTextExerciseType2.setText(temp);
                }
            }
        }
        else if (swipeDirection.equals("Swipe right")){

            if (switchTo == 0){
                if (isTime(exercise)){
                    timerButton.setVisibility(View.VISIBLE);
                    timeLeftInMiliseconds = exercise.getTime();
                    showTimedExercise(mTextExerciseType1);
                } else {
                    timerButton.setVisibility(View.INVISIBLE);
                    temp = exercise.getReps() + "";
                    mTextExerciseType1.setText(temp);
                }
            }
            else {
                mText2.setText(exercise.getName());

                if (isTime(exercise)){
                    timerButton.setVisibility(View.VISIBLE);
                    timeLeftInMiliseconds = exercise.getTime();
                    showTimedExercise(mTextExerciseType2);
                } else {
                    timerButton.setVisibility(View.INVISIBLE);
                    temp = exercise.getReps() + "";
                    mTextExerciseType2.setText(temp);
                }
            }

            // Animation
            mFlipper.setInAnimation(inFromRightAnimation());
            mFlipper.setOutAnimation(outToLeftAnimation());
            mFlipper.showPrevious();

        }
    }

    private Animation outToRightAnimation() {
        Animation outToRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outToRight.setDuration(250);
        outToRight.setInterpolator(new LinearInterpolator());
        return outToRight;

    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(250);
        inFromLeft.setInterpolator(new LinearInterpolator());
        return inFromLeft;
    }

    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(250);
        inFromRight.setInterpolator(new LinearInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation() {
        Animation outToLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outToLeft.setDuration(250);
        outToLeft.setInterpolator(new LinearInterpolator());
        return outToLeft;
    }

    private boolean isTime(Exercise exercise){
        if (exercise.getType().equals("Time")){
            return true;
        }
        return false;
    }

    // Timer methods

    private void showTimedExercise(final TextView timerText){
        updateTimer(timerText);

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop(timerText);
            }
        });
    }

    private void startStop(TextView timerText) {
        if (timeRunning){
            stopTimer();
        } else {
            startTimer(timerText);
        }
    }

    private void startTimer(final TextView timerText) {
        timer = new CountDownTimer(timeLeftInMiliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMiliseconds = millisUntilFinished;
                updateTimer(timerText);

            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerButton.setText("Stop");
        timeRunning = true;
    }

    private void updateTimer(TextView timerText) {
        int min = (int) timeLeftInMiliseconds/60000;
        int sec = (int) timeLeftInMiliseconds % 60000 / 1000;

        String timeLeftText = min + " : ";
        if(sec < 10){
            timeLeftText += "0" + sec;
        } else {
            timeLeftText += sec;
        }

        timerText.setText(timeLeftText);
    }

    private void stopTimer(){
        timer.cancel();
        timeRunning = false;
        timerButton.setText("Start");
    }
}


