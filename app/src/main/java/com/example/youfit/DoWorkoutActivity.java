package com.example.youfit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.youfit.domain.Exercise;
import com.example.youfit.domain.ExerciseElement;
import com.example.youfit.domain.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DoWorkoutActivity extends AppCompatActivity{

    private String TAG = "DoWorkoutActivity";

    private ViewFlipper flipper;
    private TextView textExerciseTime, textExerciseReps, timeText, repsText;
    private TextView textExerciseTime2, textExerciseReps2, timeText2, repsText2;
    private TextView currentExerciseTime, currentExerciseReps, currentTimeText, currentRepsText;
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

    private FloatingActionButton playPauseButton1, playPauseButton2, currentPlayPauseButton;
    private FloatingActionButton stopButton1, stopButton2, currentStopButton;

    private ProgressBar progressCircle1, progressCircle2, currentProgressCircle;

    private Chronometer totalWorkoutTime;

    private Button endWorkout;

    private ImageButton infoButton;

    private boolean isTimeView1 = false;
    private boolean isRepsView1 = false;

    private View currentViewId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

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

        //Set info button
        infoButton = findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment infoDialog = ExerciseInfoDialogFragment.newInstance(currentExercise);
                infoDialog.show(getSupportFragmentManager(), "info");
            }
        });

        /* To get smooth swipe effect no watter what exercise constellation,
        * we initialize two copies of the same views for reps and timed exercises
        */

        // View 1
        textExerciseTime = findViewById(R.id.exerciseText1);
        textExerciseReps = findViewById(R.id.exerciseText2);
        timeText = findViewById(R.id.timeText);
        repsText = findViewById(R.id.textReps);
        playPauseButton1 = findViewById(R.id.playPauseButton1);
        stopButton1 = findViewById(R.id.stopButton1);
        progressCircle1 = findViewById(R.id.progressCircle1);

        // View 2
        textExerciseTime2 = findViewById(R.id.exerciseText3);
        textExerciseReps2 = findViewById(R.id.exerciseText4);
        timeText2 = findViewById(R.id.timeText2);
        repsText2 = findViewById(R.id.textReps2);
        playPauseButton2 = findViewById(R.id.playPauseButton2);
        stopButton2 = findViewById(R.id.stopButton2);
        progressCircle2 = findViewById(R.id.progressCircle2);

        seekBar = findViewById(R.id.seekBar1);
        seekBar.setMax(exercises.size());

        totalWorkoutTime = findViewById(R.id.totalWorkoutTime1);

        endWorkout = findViewById(R.id.endWorkout);

        currentExerciseTime = new TextView(getApplicationContext());
        currentExerciseReps = new TextView(getApplicationContext());
        currentTimeText = new TextView(getApplicationContext());
        currentRepsText = new TextView(getApplicationContext());

        currentProgressCircle = progressCircle1;
        currentPlayPauseButton = playPauseButton1;
        currentStopButton = stopButton1;
        currentViewId = findViewById(R.id.viewTime);

    }

    private void changeView(){

        if (isTime() || isPause()){
            if (isTimeView1){
                currentViewId = findViewById(R.id.viewTime2);
                currentPlayPauseButton = playPauseButton2;
                currentStopButton = stopButton2;
                currentExerciseTime = textExerciseTime2;
                currentProgressCircle = progressCircle2;
                isTimeView1 = false;
            }
            else {
                currentViewId = findViewById(R.id.viewTime);
                currentPlayPauseButton = playPauseButton1;
                currentStopButton = stopButton1;
                currentExerciseTime = textExerciseTime;
                currentProgressCircle = progressCircle1;
                isTimeView1 = true;
            }
        }
        else {
            if (isRepsView1){
                currentViewId = findViewById(R.id.viewReps2);
                currentExerciseReps = textExerciseReps2;
                currentRepsText = repsText2;
                isRepsView1 = false;
            }
            else {
                currentViewId = findViewById(R.id.viewReps);
                currentExerciseReps = textExerciseReps;
                currentRepsText = repsText;
                isRepsView1 = true;
            }
        }
    }

    private void swiped(String swipeDirection){
        seekBar.setProgress(counter);
        currentProgressCircle.setProgress(0);

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

            changeView();

            currentExerciseTime.setText(currentExercise.getName());
            if (timeRunning){
                resetTimer();
            }
            flipper.setDisplayedChild(flipper.indexOfChild(currentViewId));
            timerExercise();

        }
        else if (isPause()){
            changeView();

            currentExerciseTime.setText(currentExercise.getName());
            if (timeRunning){
                resetTimer();
            }
            flipper.setDisplayedChild(flipper.indexOfChild(currentViewId));
            pause();

        }
        else if (isReps()){
            changeView();

            currentExerciseReps.setText(currentExercise.getName());
            flipper.setDisplayedChild(flipper.indexOfChild(currentViewId));
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
        currentProgressCircle.setMax((int) START_TIME - 1000);
        currentProgressCircle.setProgress(0);
        currentPlayPauseButton.setVisibility(View.VISIBLE);
        currentStopButton.setVisibility(View.VISIBLE);
        showTimedExercise();
    }

    public void repsExercise(){
        temp = currentExercise.getAmountString() + "";
        currentRepsText.setText(temp);
    }

    public void pause(){
        timeLeftInMilliseconds = Integer.parseInt(currentExercise.getAmountString());
        START_TIME = timeLeftInMilliseconds;
        currentProgressCircle.setMax((int) START_TIME - 1000);
        currentProgressCircle.setProgress(0);
        currentPlayPauseButton.setVisibility(View.INVISIBLE);
        currentStopButton.setVisibility(View.INVISIBLE);
        showTimedExercise();
        startTimer();
    }

    // Timer methods

    private void showTimedExercise(){
        updateTimer();

        if (isTime()){
            currentPlayPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!timeRunning){
                        startTimer();
                        totalWorkoutTime.start();
                        currentPlayPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    }
                    else {
                        stopTimer();
                        totalWorkoutTime.stop();
                        currentPlayPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                }
            });

            currentStopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTimer();
                }
            });
        }

    }

    private void resetTimer() {
        currentProgressCircle.setProgress(0);
        timeLeftInMilliseconds = START_TIME;
        updateTimer();
        currentPlayPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        stopTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateProgressBar(millisUntilFinished);
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                currentProgressCircle.setProgress(currentProgressCircle.getMax());
                currentPlayPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                timeRunning = false;
            }
        }.start();
        timeRunning = true;
    }

    private void updateProgressBar(long millisUntilFinished){
        currentProgressCircle.setProgress((int) START_TIME - (int) millisUntilFinished);
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

        if (isTimeView1){
            timeText.setText(timeLeftText);
        }
        else {
            timeText2.setText(timeLeftText);
        }
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


