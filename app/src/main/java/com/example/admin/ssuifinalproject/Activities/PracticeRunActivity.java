package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.PracticeRun;
import com.example.admin.ssuifinalproject.R;

public class PracticeRunActivity extends AppCompatActivity {

    int practiceRun_id;
    DatabaseHelper db;
    PracticeRun practiceRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_run);

        // Retrieve data from intent
        Intent intent = getIntent();
        practiceRun_id = intent.getIntExtra("practiceRun_id", -1);

        db = new DatabaseHelper(this);
        practiceRun = db.getPracticeRun(practiceRun_id);
    }
}
