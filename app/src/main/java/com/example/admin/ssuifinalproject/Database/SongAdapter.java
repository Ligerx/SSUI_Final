package com.example.admin.ssuifinalproject.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.Database.Models.Song;
import com.example.admin.ssuifinalproject.R;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter{
    public SongAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Song song = (Song) getItem(position);
        int id = song.getId();

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_item, parent, false);
        }

        // Lookup view for data population
        TextView songTitle = (TextView) convertView.findViewById(R.id.songTitle);
        TextView practiceRunCount = (TextView) convertView.findViewById(R.id.practiceRunCount);

        // Populate the data into the template view using the data object
        songTitle.setText(song.getTitle());

        DatabaseHelper db = new DatabaseHelper(getContext());
        int numPracticeRuns = db.findAllPracticeRunsBySong(id).size();
        practiceRunCount.setText(String.valueOf(numPracticeRuns));

        // Return the completed view to render on screen
        return convertView;
    }
}
