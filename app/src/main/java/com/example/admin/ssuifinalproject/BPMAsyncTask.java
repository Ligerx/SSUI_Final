package com.example.admin.ssuifinalproject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.admin.ssuifinalproject.Database.DatabaseHelper;
import com.example.admin.ssuifinalproject.Database.Models.BPM;
import com.example.admin.ssuifinalproject.Database.Models.PracticeRun;

import java.util.ArrayList;

public class BPMAsyncTask extends AsyncTask<String, Void, BeatAnalyzer> {

    private Context context;
    private int song_id;
    private String filePath;
    private int targetBPM;

    public BPMAsyncTask(Context context, int song_id, String filePath, int targetBPM) {
        this.context = context;
        this.song_id = song_id;
        this.filePath = filePath;
        this.targetBPM = targetBPM;
    }

    protected BeatAnalyzer doInBackground(String... args) {
        // Should be path to audio file
        String path = args[0];
        return new BeatAnalyzer(path, context);
    }

    protected void onPostExecute(BeatAnalyzer beatAnalyzer) {
        DatabaseHelper db = new DatabaseHelper(context);
        BeatData beatData = beatAnalyzer.getBeatData();

        ArrayList<Double> timings = beatData.getBeatTiming();
        double medianBPM = beatData.getMedianBPM();

        // Put this practice run in db
        PracticeRun practiceRun = new PracticeRun(song_id, targetBPM, medianBPM, beatData);
        practiceRun.setFilePath(filePath); // was tacked on later cause I forgot
        int practiceRun_id = (int) db.createPracticeRun(practiceRun);

        // add all the timings/bpm to db
        for(double timing : timings) {
            BPM bpm = new BPM(practiceRun_id, timing);
            db.createBPM(bpm);
        }

        ((Activity) context).finish();
    }
}
