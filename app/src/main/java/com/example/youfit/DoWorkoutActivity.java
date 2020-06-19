package com.example.youfit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.youfit.domain.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DoWorkoutActivity extends AppCompatActivity{

    private String TAG = "DoWorkoutActivity";

    private ViewFlipper flipper;
    private TextView exerciseName1, exerciseName2, exerciseType1, exerciseType2;
    private int counter, currentLayoutState;

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
    private TextView currentTextViewExerciseType;

    private FloatingActionButton playPauseButton;
    private FloatingActionButton stopButton;

    private ProgressBar progressCircle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        // Initialization of stuff
        init();

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

        exerciseName1.setText(currentExercise.getName());
        currentTextViewExerciseType = exerciseType1;

        if (isPause()){
            pause();
        }
        if (isTime()) {
            timerExercise();
        } else {
            repsExercise();
        }

        // Gesture detector detects the swipe motion
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityX < -10.0f && counter < exercises.size()-1 && counter >= 0) { // Swipe right
                        counter++;
                        progressCircle.setProgress(0);
                        swiped("Swipe right");

                    } else if (velocityX > 10.0f && counter > 0) { // Swipe left
                        counter--;
                        progressCircle.setProgress(0);
                        swiped("Swipe left");
                    }
                return true;
            }

        });
    }

    private void swiped(String swipeDirection){
        seekBar.setProgress(counter);
        currentExercise = exercises.get(counter);

        // if mCurrentLayoutState == 0, then mCurrentLayoutState is set to 1, otherwise 0.
        currentLayoutState = (currentLayoutState == 0) ? 1 : 0;

        switchLayoutStateTo(currentLayoutState, swipeDirection);
    }

    private void init(){
        workout = getIntent().getParcelableExtra("workout");
        exercises = workout.getExercises();

        // Determines which layout to display
        currentLayoutState = 0;

        // Determines which exercise, we have reached
        counter = 0;

        flipper = (ViewFlipper) findViewById(R.id.view_flipper);
        exerciseName1 = (TextView) findViewById(R.id.textView1);
        exerciseName2 = (TextView) findViewById(R.id.textView3);
        exerciseType1 = (TextView) findViewById(R.id.textViewExerciseType1);
        exerciseType2 = (TextView) findViewById(R.id.textViewExerciseType2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(exercises.size()-1);
        currentExercise = exercises.get(0);

        currentTextViewExerciseType = new TextView(getApplicationContext());

        playPauseButton = (FloatingActionButton) findViewById(R.id.playPauseButton);
        stopButton = (FloatingActionButton) findViewById(R.id.stopButton);

        progressCircle = findViewById(R.id.progressCircle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void switchLayoutStateTo(int switchTo, String swipeDirection) {

        if (swipeDirection.equals("Swipe left")){

            // Animation
            flipper.setInAnimation(inFromLeftAnimation());
            flipper.setOutAnimation(outToRightAnimation());
            flipper.showPrevious();

            if (switchTo == 0){
                update(exerciseName1, exerciseType1);
            }
            else {
                update(exerciseName2, exerciseType2);
            }
        }
        else if (swipeDirection.equals("Swipe right")){

            // Animation
            flipper.setInAnimation(inFromRightAnimation());
            flipper.setOutAnimation(outToLeftAnimation());
            flipper.showPrevious();

            if (switchTo == 0){
                update(exerciseName1, exerciseType1);
            }
            else {
                update(exerciseName2, exerciseType2);
            }
        }
    }

    private void update(TextView name, TextView type){
        name.setText(currentExercise.getName());
        currentTextViewExerciseType = type;

        if (isPause()){
            pause();

        } else if (isTime()){
            timerExercise();

        } else {
            repsExercise();
        }
    }
    

    private void endOfBreak(){
        counter++;
        seekBar.setProgress(counter);
        currentExercise = exercises.get(counter);

        currentLayoutState = (currentLayoutState == 0) ? 1 : 0;

        switchLayoutStateTo(currentLayoutState, "Swipe right");

    }

    private boolean isPause() {
        if (currentExercise.getType().equals("Pause")){
            return true;
        }
        return false;
    }

    public void timerExercise(){
        timeLeftInMilliseconds = Integer.parseInt(currentExercise.getAmountString());
        progressCircle.setMax((int) timeLeftInMilliseconds - 1000);
        progressCircle.setVisibility(View.VISIBLE);
        playPauseButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        START_TIME = timeLeftInMilliseconds;
        showTimedExercise();
    }

    public void repsExercise(){
        progressCircle.setVisibility(View.INVISIBLE);
        playPauseButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        temp = currentExercise.getAmountString();
        currentTextViewExerciseType.setText(temp);
    }

    public void pause(){
        timeLeftInMilliseconds = Integer.parseInt(currentExercise.getAmountString());
        START_TIME = timeLeftInMilliseconds;
        progressCircle.setMax((int) timeLeftInMilliseconds - 1000);
        progressCircle.setProgress(0);
        progressCircle.setVisibility(View.VISIBLE);
        playPauseButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        showTimedExercise();
        startTimer();
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

    private boolean isTime(){
        if (currentExercise.getType().equals("Time")){
            return true;
        }
        return false;
    }

    private void showTimedExercise(){
        updateTimer(currentTextViewExerciseType);

        if (currentExercise.getType().equals("Time")){
            playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!timeRunning){
                        startTimer();
                        playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    }
                    else {
                        stopTimer();
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
        updateTimer(currentTextViewExerciseType);
        playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        stopTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateProgressBer(millisUntilFinished);
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer(currentTextViewExerciseType);
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

    private void updateTimer(TextView timerText) {
        int min = (int) timeLeftInMilliseconds / 60000;
        int sec = (int) timeLeftInMilliseconds % 60000 / 1000; // Works as long as we don't go above 1000 min

        String timeLeftText = min + " : ";
        if(sec < 10){
            timeLeftText += "0" + sec;
        } else {
            timeLeftText += sec;
        }

        timerText.setText(timeLeftText);

        if (currentExercise.getType().equals("Pause") && timeLeftInMilliseconds < 1000){
            endOfBreak();
        }
    }

    private void stopTimer(){
        timer.cancel();
        timeRunning = false;
    }
}


