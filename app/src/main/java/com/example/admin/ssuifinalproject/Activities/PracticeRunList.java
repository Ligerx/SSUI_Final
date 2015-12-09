package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.Song;
import com.example.admin.ssuifinalproject.R;

public class PracticeRunList extends AppCompatActivity {

    int NEW_PRACTICE_RUN = 5;

    Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_run_list);

        Intent intent = getIntent();
        int song_id = intent.getIntExtra("song_id", -1);

        DatabaseHelper db = new DatabaseHelper(this);
        song = db.getSong(song_id);

        TextView testText = (TextView) findViewById(R.id.testText);
        testText.setText(song.getTitle());

        Button newPracticeRunButton = (Button) findViewById(R.id.newPracticeRunButton);
        newPracticeRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Practice run", "on click. working!");
                recordNewPracticeRun();
            }
        });
    }

    private void recordNewPracticeRun() {
        Intent newPracticeRun = new Intent(PracticeRunList.this, RecordPracticeRunActivity.class);
        newPracticeRun.putExtra("song_id", song.getId());
        newPracticeRun.putExtra("targetBPM", song.getTargetBPM());

        startActivityForResult(newPracticeRun, NEW_PRACTICE_RUN);
    }
}
