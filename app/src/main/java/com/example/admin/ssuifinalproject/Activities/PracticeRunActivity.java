package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.AsyncChartSetter;
import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.PracticeRun;
import com.example.admin.ssuifinalproject.R;
import com.github.mikephil.charting.charts.Chart;

import java.io.IOException;

public class PracticeRunActivity extends AppCompatActivity {

    int practiceRun_id;
    DatabaseHelper db;
    PracticeRun practiceRun;

    boolean isPrepared = false;
    boolean isPlaying = false;
    MediaPlayer player;

    Button practiceRunPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_run);

        // Retrieve data from intent
        Intent intent = getIntent();
        practiceRun_id = intent.getIntExtra("practiceRun_id", -1);

        db = new DatabaseHelper(this);
        practiceRun = db.getPracticeRun(practiceRun_id);


        TextView practiceRunFileName = (TextView) findViewById(R.id.practiceRunFileName);
        Chart practiceRunChart = (Chart) findViewById(R.id.practiceRunChart);
        TextView practiceRunBPM = (TextView) findViewById(R.id.practiceRunBPM);
        practiceRunPlayButton = (Button) findViewById(R.id.practiceRunPlayButton);

        practiceRunFileName.setText(practiceRun.getFileName());
        practiceRunBPM.setText("Median BPM: " + practiceRun.getMedianBPM());

        // setup the chart
        new AsyncChartSetter(practiceRunChart).execute(practiceRun.getBeatData());

        //media player stuff goes here
        player = new MediaPlayer();
        try {
            player.setDataSource(this, Uri.parse(practiceRun.getFilePath()));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                finishedPreparing();
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioFinished();
            }
        });


        practiceRunPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressPlayButton();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) player.release();
    }

    private void finishedPreparing() {
        isPrepared = true;
    }

    private void pressPlayButton() {
        if(!isPrepared) { return; }

        if(!isPlaying){
            // play
            isPlaying = true;
            player.start();
            practiceRunPlayButton.setText("Pause");

        }
        else {
            // pause or stop? i guess pause
            isPlaying = false;
            player.pause();
            practiceRunPlayButton.setText("Resume");
        }
    }

    private void audioFinished() {
        isPlaying = false;
        practiceRunPlayButton.setText("Listen to recording again");
    }
}
