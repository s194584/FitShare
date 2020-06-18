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
import com.example.youfit.domain.Workout;

import java.util.ArrayList;

public class DoWorkoutActivity extends AppCompatActivity{

    private ViewFlipper mFlipper;
    private TextView mText1, mText2, mTextExerciseType1, mTextExerciseType2;
    private int mCount, mCurrentLayoutState;

    private SeekBar seekBar;

    private GestureDetector mGesture;

    private CountDownTimer timer;
    private long timeLeftInMilliseconds;
    private boolean timeRunning = false;
    private Button timerButton;

    private String temp;

    ArrayList<Exercise> exercises = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        Intent intent = getIntent();
        Workout workout = intent.getParcelableExtra("workout");

        exercises = workout.getExercises();

        // Determines which layout to display
        mCurrentLayoutState = 0;

        // Determines which exercise, we have reached
        mCount = 0;

        // Initialization of stuff
        init();

        seekBar.setMax(exercises.size()-1);

        /* OnTouchListener makes sure, client cant change seekBar
           as well as maintaining color.
           setEnable(false) would make seekBar colorless.
         */
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        mText1.setText(exercises.get(0).getName());

        if (isTime(exercises.get(0))){
            timerExercise(mTextExerciseType1, exercises.get(0));
        } else {
            repsExercise(mTextExerciseType1, exercises.get(0));
        }

        // Gesture detector detects the swipe motion
        mGesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityX < -10.0f && mCount < exercises.size()-1 && mCount >= 0) { // Swipe right
                        mCount++;

                        seekBar.setProgress(mCount);

                        // if mCurrentLayoutState == 0, then mCurrentLayoutState is set to 1, otherwise 0.
                        mCurrentLayoutState = (mCurrentLayoutState == 0) ? 1 : 0;

                        switchLayoutStateTo(mCurrentLayoutState, exercises.get(mCount), "Swipe right");

                    } else if (velocityX > 10.0f && mCount > 0) { // Swipe left
                        mCount--;

                        seekBar.setProgress(mCount);

                        mCurrentLayoutState = (mCurrentLayoutState == 0) ? 1 : 0;

                        switchLayoutStateTo(mCurrentLayoutState, exercises.get(mCount), "Swipe left");

                    }
                return true;
            }

        });
    }

    private void init(){
        mFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        mText1 = (TextView) findViewById(R.id.textView1);
        mText2 = (TextView) findViewById(R.id.textView3);
        mTextExerciseType1 = (TextView) findViewById(R.id.textViewExerciseType1);
        mTextExerciseType2 = (TextView) findViewById(R.id.textViewExerciseType2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        timerButton = findViewById(R.id.timerButton);
        timerButton.setVisibility(View.INVISIBLE);
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

                if (isPause(exercise)){
                    pause(mTextExerciseType1, exercise);

                } else if (isTime(exercise)){
                    timerExercise(mTextExerciseType1, exercise);

                } else {
                    repsExercise(mTextExerciseType1, exercise);
                }

            }
            else {
                mText2.setText(exercise.getName());

                if (isPause(exercise)){
                    pause(mTextExerciseType2, exercise);

                } else if (isTime(exercise)){
                    timerExercise(mTextExerciseType2, exercise);

                } else {
                    repsExercise(mTextExerciseType2, exercise);
                }
            }
        }
        else if (swipeDirection.equals("Swipe right")){

            // Animation
            mFlipper.setInAnimation(inFromRightAnimation());
            mFlipper.setOutAnimation(outToLeftAnimation());
            mFlipper.showPrevious();

            if (switchTo == 0){
                mText1.setText(exercise.getName());

                if (isPause(exercise)) {
                    pause(mTextExerciseType1, exercise);

                } else if (isTime(exercise)){
                    timerExercise(mTextExerciseType1, exercise);

                } else {
                    repsExercise(mTextExerciseType1, exercise);
                }
            }
            else {
                mText2.setText(exercise.getName());

                if (isPause(exercise)){
                    pause(mTextExerciseType2, exercise);

                } else if (isTime(exercise)){
                    timerExercise(mTextExerciseType2, exercise);

                } else {
                    repsExercise(mTextExerciseType2, exercise);
                }
            }
        }
    }

    private void takeABreak(){
        mCount++;
        seekBar.setProgress(mCount);

        mCurrentLayoutState = (mCurrentLayoutState == 0) ? 1 : 0;

        switchLayoutStateTo(mCurrentLayoutState, exercises.get(mCount), "Swipe right");

    }

    private boolean isPause(Exercise exercise) {
        if (exercise.getType().equals("Pause")){
            return true;
        }
        return false;
    }

    public void timerExercise(TextView text, Exercise exercise){
        timerButton.setVisibility(View.VISIBLE);
        timeLeftInMilliseconds = exercise.getTime();
        showTimedExercise(text, exercise);
    }

    public void repsExercise(TextView text, Exercise exercise){
        timerButton.setVisibility(View.INVISIBLE);
        temp = exercise.getReps() + "";
        text.setText(temp);
    }

    public void pause(TextView text, Exercise exercise){
        timerButton.setVisibility(View.INVISIBLE);
        timeLeftInMilliseconds = exercise.getTime();
        showTimedExercise(text, exercise);
        startTimer(text, exercise);
    }

    // Animation methods

    private Animation outToRightAnimation() {
        Animation outToRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outToRight.setDuration(250);
        outToRight.setInterpolator(new LinearInterpolator());

        if (timeRunning){
            stopTimer();
        }
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

        if(timeRunning){
            stopTimer();
        }
        return outToLeft;
    }

    // Timer methods

    private boolean isTime(Exercise exercise){
        if (exercise.getType().equals("Time")){
            return true;
        }
        return false;
    }

    private void showTimedExercise(final TextView timerText, final Exercise exercise){
        updateTimer(timerText, exercise);

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop(timerText, exercise);
            }
        });
    }

    private void startStop(TextView timerText, Exercise exercise) {
        if (timeRunning){
            stopTimer();
        } else {
            startTimer(timerText, exercise);
        }
    }

    private void startTimer(final TextView timerText, final Exercise exercise) {
        timer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("DoWorkoutActivity", "Updating timeLeftInMilliSeconds");
                timeLeftInMilliseconds = millisUntilFinished;
                System.out.println(timeLeftInMilliseconds);
                updateTimer(timerText, exercise);
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerButton.setText("Stop");
        timeRunning = true;
    }

    private void updateTimer(TextView timerText, Exercise exercise) {
        int min = (int) timeLeftInMilliseconds / 60000;
        int sec = (int) timeLeftInMilliseconds % 60000 / 1000; // Works as long as we don't go above 1000 min

        String timeLeftText = min + " : ";
        if(sec < 10){
            timeLeftText += "0" + sec;
        } else {
            timeLeftText += sec;
        }

        timerText.setText(timeLeftText);

        if (exercise.getType().equals("Pause") && timeLeftInMilliseconds < 1000){
            takeABreak();
        }

    }

    private void stopTimer(){
        timer.cancel();
        timeRunning = false;
        timerButton.setText("Start");
    }
}


