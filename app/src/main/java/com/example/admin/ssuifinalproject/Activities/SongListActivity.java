package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.Song;
import com.example.admin.ssuifinalproject.Database.SongAdapter;
import com.example.admin.ssuifinalproject.R;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    static final int NEW_SONG_REQUEST = 1;  // The request code

    SongAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        db = new DatabaseHelper(this);
        ArrayList<Song> songs = db.getAllSongs();

        // Create the adapter to convert the array to views
        adapter = new SongAdapter(this, songs);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.songList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song songClicked = (Song) parent.getAdapter().getItem(position);
                viewSongInfo(songClicked);
            }
        });

        Button newSongButton = (Button) findViewById(R.id.newSongButton);
        newSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewSongForm();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Force refresh of list
        ArrayList<Song> updatedSongs = db.getAllSongs();
        adapter.clear();
        adapter.addAll(updatedSongs);
        adapter.notifyDataSetChanged();
    }

    private void viewSongInfo(Song songClicked) {
        Intent practiceRunListActivity = new Intent(SongListActivity.this, PracticeRunList.class);
        practiceRunListActivity.putExtra("song_id", songClicked.getId());

        startActivity(practiceRunListActivity);
    }

    private void gotoNewSongForm() {
        Intent newSongActivity = new Intent(SongListActivity.this, SongForm.class);
        startActivityForResult(newSongActivity, NEW_SONG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_SONG_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int song_id = data.getIntExtra("song_id", NEW_SONG_REQUEST);
                Log.d("BLARG", String.valueOf(song_id));

                Song newSong = db.getSong(song_id);
                adapter.add(newSong);
            }
        }
    }
}
