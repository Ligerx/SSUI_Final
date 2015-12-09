package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.PracticeRun;
import com.example.admin.ssuifinalproject.Database.Models.Song;
import com.example.admin.ssuifinalproject.Database.PracticeRunAdapter;
import com.example.admin.ssuifinalproject.R;

import java.util.ArrayList;

public class PracticeRunList extends AppCompatActivity {

    String TAG = "PracticeRunList";

    int NEW_PRACTICE_RUN = 5;

    int song_id;
    Song song;
    ArrayList<PracticeRun> practiceRuns;
    PracticeRunAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_run_list);

        // Retrieve data from intent
        Intent intent = getIntent();
        song_id = intent.getIntExtra("song_id", -1);

        Log.d(TAG, "Currently viewing song id: " + song_id);

        // Get models from db
        db = new DatabaseHelper(this);
        song = db.getSong(song_id);
        practiceRuns = db.findAllPracticeRunsBySong(song_id);

        Log.d(TAG, "song_id:" + String.valueOf(song_id));
        Log.d(TAG, "practiceRuns size: " + String.valueOf(practiceRuns.size()));

        // Fill list adapter
        adapter = new PracticeRunAdapter(this, practiceRuns);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.practiceRunList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PracticeRun practiceRunClicked = (PracticeRun) parent.getAdapter().getItem(position);
                viewSinglePracticeRun(practiceRunClicked);
            }
        });

        // Find ui elements
        TextView testText = (TextView) findViewById(R.id.testText);
        testText.setText(song.getTitle());

        // set new practice run button onclick listener
        Button newPracticeRunButton = (Button) findViewById(R.id.newPracticeRunButton);
        newPracticeRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Practice run", "on click. working!");
                recordNewPracticeRun();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // rerun the query?
        practiceRuns = db.findAllPracticeRunsBySong(song_id);
        adapter.clear();
        adapter.addAll(practiceRuns);
        adapter.notifyDataSetChanged();
    }

    private void viewSinglePracticeRun(PracticeRun practiceRunClicked) {
        // TODO
    }

    private void recordNewPracticeRun() {
        Intent newPracticeRun = new Intent(PracticeRunList.this, RecordPracticeRunActivity.class);
        newPracticeRun.putExtra("song_id", song.getId());
        newPracticeRun.putExtra("targetBPM", song.getTargetBPM());

        startActivityForResult(newPracticeRun, NEW_PRACTICE_RUN);
    }
}
