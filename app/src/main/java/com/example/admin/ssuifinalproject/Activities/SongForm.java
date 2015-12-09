package com.example.admin.ssuifinalproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.Song;
import com.example.admin.ssuifinalproject.R;

public class SongForm extends AppCompatActivity {

    EditText songTitleField;
    EditText songBPMField;
    Button createSongButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_form);

        songTitleField = (EditText) findViewById(R.id.songTitleField);
        songBPMField = (EditText) findViewById(R.id.songBPMField);

        createSongButton = (Button) findViewById(R.id.createSongButton);

        createSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewSong();
            }
        });

    }

    private void createNewSong() {
        String title = songTitleField.getText().toString();
        String bpmString = songBPMField.getText().toString();

        if(!validateNewSong(title, bpmString)) { return; }

        Song song = new Song(title,
                Integer.parseInt(bpmString));

        DatabaseHelper db = new DatabaseHelper(this);
        int id = (int) db.createSong(song);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("song_id", id);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private boolean validateNewSong(String title, String bpmString) {
        if(title == null || title.equals("")) {
            return false;
        }
        else if(bpmString == null || bpmString.equals("") || Integer.parseInt(bpmString) <= 0) {
            return false;
        }
        else {
            return true;
        }
    }
}
