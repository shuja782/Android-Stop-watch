package com.example.timerwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView timerText;
    private FloatingActionButton playButton, stopButton, restartButton;
    private Handler handler;
    private long startTime = 0L;
    private long elapsedTime = 0L;
    private final int REFRESH_RATE = 100;
    private int lapCount = 1;
    private TextView[] lapTextViews = new TextView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timer_text);
        playButton = findViewById(R.id.play_button);
        stopButton = findViewById(R.id.stop_button);
        restartButton = findViewById(R.id.restart_button);
        handler = new Handler();

        lapTextViews[0] = findViewById(R.id.lap_time_1);
        lapTextViews[1] = findViewById(R.id.lap_time_2);
        lapTextViews[2] = findViewById(R.id.lap_time_3);
        lapTextViews[3] = findViewById(R.id.lap_time_4);
        lapTextViews[4] = findViewById(R.id.lap_time_5);
        lapTextViews[5] = findViewById(R.id.lap_time_6);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartTimer();
            }
        });
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            elapsedTime = SystemClock.uptimeMillis() - startTime;
            long updatedTime = elapsedTime;
            int seconds = (int) (updatedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, REFRESH_RATE);
        }
    };

    private void startTimer() {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimer, 0);
        playButton.setEnabled(false);
        restartButton.setEnabled(false);
    }

    private void stopTimer() {
        handler.removeCallbacks(updateTimer);
        playButton.setEnabled(true);
        restartButton.setEnabled(true);

        if (lapCount <= 6) {
            String lapTime = timerText.getText().toString();
            String lapTimeWithPrefix = getString(R.string.lap_prefix, lapCount) + lapTime;
            lapTextViews[lapCount - 1].setText(lapTimeWithPrefix);
            lapCount++;
        }
    }

    private void restartTimer() {
        handler.removeCallbacks(updateTimer);
        timerText.setText(getString(R.string._00_00_00));
        playButton.setEnabled(true);
        restartButton.setEnabled(false);
        lapCount = 1;
        for (int i = 0; i < lapTextViews.length; i++) {
            lapTextViews[i].setText(getString(R.string.lap_prefix, i + 1) + getString(R.string.lap_time_default));
        }
    }



}
