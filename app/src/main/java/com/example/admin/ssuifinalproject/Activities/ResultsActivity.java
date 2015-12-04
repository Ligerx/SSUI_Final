package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.ssuifinalproject.BPMAsyncTask;
import com.example.admin.ssuifinalproject.R;

public class ResultsActivity extends AppCompatActivity {

//    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

//        chart = (LineChart) findViewById(R.id.chart);

        Intent i = getIntent();
        String audioFilePath = i.getStringExtra("filePath");

        new BPMAsyncTask(this).execute(audioFilePath);
    }
}
