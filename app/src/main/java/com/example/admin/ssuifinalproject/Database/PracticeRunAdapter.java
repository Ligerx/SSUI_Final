package com.example.admin.ssuifinalproject.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.Database.Models.PracticeRun;
import com.example.admin.ssuifinalproject.R;

import java.util.ArrayList;

public class PracticeRunAdapter extends ArrayAdapter {
    public PracticeRunAdapter(Context context, ArrayList<PracticeRun> practiceRuns) {
        super(context, 0, practiceRuns);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PracticeRun practiceRun = (PracticeRun) getItem(position);
        int id = practiceRun.getId();

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.practice_run_item, parent, false);
        }

        // Lookup view for data population
        TextView practiceRunFileName = (TextView) convertView.findViewById(R.id.practiceRunFileName);
        TextView practiceRunMedianBPM = (TextView) convertView.findViewById(R.id.practiceRunMedianBPM);

        // Populate the data into the template view using the data object
        practiceRunFileName.setText(practiceRun.getFilePath());
        practiceRunMedianBPM.setText(String.valueOf(practiceRun.getMedianBPM()));

        // Return the completed view to render on screen
        return convertView;
    }
}
