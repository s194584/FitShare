package com.example.youfit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseElement;
import com.example.youfit.domain.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DoWorkoutActivity extends AppCompatActivity{

    private String TAG = "DoWorkoutActivity";

    private ViewFlipper flipper;
    private TextView textExerciseTime, textExerciseReps, timeText, repsText;
    private int counter;

    private SeekBar seekBar;

    private GestureDetector gestureDetector;

    private CountDownTimer timer;
    private long timeLeftInMilliseconds;
    private long START_TIME;
    private boolean timeRunning = false;

    private String temp;

    private ArrayList<Exercise> exercises = new ArrayList<>();
    private Workout workout;
    private Exercise currentExercise;

    private FloatingActionButton playPauseButton;
    private FloatingActionButton stopButton;

    private ProgressBar progressCircle;

    private Chronometer totalWorkoutTime;

    private Button endWorkout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout2);

        // Initialization of stuff
        init();

        /* OnTouchListener makes sure, client can't change seekBar
           as well as maintaining seekBar's color.
           setEnable(false) would make seekBar colorless.
         */
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        totalWorkoutTime.start();

        updateView();

        // Gesture detector detects the swipe motion
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityX < -10.0f && counter < exercises.size() && counter >= 0) { // Swipe right
                        if (counter == exercises.size()-1){
                            counter++;
                            showWorkoutFinished();
                        } else {
                            counter++;
                            currentExercise = exercises.get(counter);
                            swiped("right");
                        }

                    } else if (velocityX > 10.0f && counter > 0) { // Swipe left
                        counter--;
                        currentExercise = exercises.get(counter);
                        swiped("left");
                    }
                return true;
            }

        });
    }

    private void init(){
        workout = getIntent().getParcelableExtra("workout");
        exercises = workout.getExercises();

        currentExercise = exercises.get(0);

        // Determines which exercise, we have reached
        counter = 0;

        flipper = findViewById(R.id.view_flipper1);
        textExerciseTime = findViewById(R.id.exerciseText1);
        textExerciseReps = findViewById(R.id.exerciseText2);
        timeText = findViewById(R.id.timeText);
        repsText = findViewById(R.id.textReps);

        seekBar = findViewById(R.id.seekBar1);
        seekBar.setMax(exercises.size());

        playPauseButton = findViewById(R.id.playPauseButton1);
        stopButton = findViewById(R.id.stopButton1);
        progressCircle = findViewById(R.id.progressCircle1);
        totalWorkoutTime = findViewById(R.id.totalWorkoutTime1);

        endWorkout = findViewById(R.id.endWorkout);

    }

    private void swiped(String swipeDirection){
        seekBar.setProgress(counter);
        progressCircle.setProgress(0);

        if (swipeDirection.equals("left")){
            swipeLeft();
            updateView();
        }
        else if (swipeDirection.equals("right")){
            swipeRight();
            updateView();
        }
    }

    private void updateView() {
        if (isTime()){
            textExerciseTime.setText(currentExercise.getName());
            if (timeRunning){
                resetTimer();
            }
            flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.viewTime)));
            timerExercise();

        }
        else if (isPause()){
            textExerciseTime.setText(currentExercise.getName());
            if (timeRunning){
                resetTimer();
            }
            flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.viewTime)));
            pause();

        }
        else if (isReps()){
            textExerciseReps.setText(currentExercise.getName());
            flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.viewReps)));
            repsExercise();
        }
    }

    // Animation methods. NOTE: Android does not have a swipe right animation per default
    private void swipeLeft(){
        flipper.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        flipper.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);
    }

    private void swipeRight(){
        flipper.setInAnimation(getApplicationContext(), R.anim.slide_in_right);
        flipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private boolean isPause() {
        return currentExercise.getType().equals("PAUSE");
    }

    private boolean isReps(){
        return currentExercise.getType().equals("REPETITION");
    }

    private boolean isTime(){
        return currentExercise.getType().equals("TIME");
    }

    public void timerExercise(){
        timeLeftInMilliseconds = currentExercise.getAmount();
        START_TIME = timeLeftInMilliseconds;
        progressCircle.setMax((int) START_TIME - 1000);
        progressCircle.setProgress(0);
        playPauseButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        showTimedExercise();
    }

    public void repsExercise(){
        temp = currentExercise.getAmountString() + "";
        repsText.setText(temp);
    }

    public void pause(){
        timeLeftInMilliseconds = Integer.parseInt(currentExercise.getAmountString());
        START_TIME = timeLeftInMilliseconds;
        progressCircle.setMax((int) START_TIME - 1000);
        progressCircle.setProgress(0);
        playPauseButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        showTimedExercise();
        startTimer();
    }

    // Timer methods

    private void showTimedExercise(){
        updateTimer();

        if (isTime()){
            playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!timeRunning){
                        startTimer();
                        totalWorkoutTime.start();
                        playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    }
                    else {
                        stopTimer();
                        totalWorkoutTime.stop();
                        playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                }
            });

            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTimer();
                }
            });
        }

    }

    private void resetTimer() {
        progressCircle.setProgress(0);
        timeLeftInMilliseconds = START_TIME;
        updateTimer();
        playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        stopTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateProgressBer(millisUntilFinished);
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                progressCircle.setProgress(progressCircle.getMax());
                playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                timeRunning = false;
            }
        }.start();
        timeRunning = true;
    }

    private void updateProgressBer(long millisUntilFinished){
        progressCircle.setProgress((int) START_TIME - (int) millisUntilFinished);
    }

    private void updateTimer() {
        int min = (int) timeLeftInMilliseconds / 60000;
        int sec = (int) timeLeftInMilliseconds % 60000 / 1000; // Works as long as we don't go above 1000 min

        String timeLeftText = min + " : ";
        if(sec < 10){
            timeLeftText += "0" + sec;
        } else {
            timeLeftText += sec;
        }

        timeText.setText(timeLeftText);
    }

    private void stopTimer(){
        timer.cancel();
        timeRunning = false;
    }

    private void showWorkoutFinished() {
        swipeRight();
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.viewFinished)));

        endWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


