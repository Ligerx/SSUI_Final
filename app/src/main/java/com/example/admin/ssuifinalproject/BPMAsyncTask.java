package com.example.admin.ssuifinalproject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class BPMAsyncTask extends AsyncTask<String, Void, BeatAnalyzer> {

    private Context context;

    public BPMAsyncTask(Context context) {
        this.context = context;
    }

    protected BeatAnalyzer doInBackground(String... args) {
//        int count = urls.length;
//        long totalSize = 0;
//        for (int i = 0; i < count; i++) {
//            totalSize += Downloader.downloadFile(urls[i]);
//            publishProgress((int) ((i / (float) count) * 100));
//            // Escape early if cancel() is called
//            if (isCancelled()) break;
//        }
//        return totalSize;

        // Should be path to audio file
        String path = args[0];
        return new BeatAnalyzer(path, context);
    }

    protected void onPostExecute(BeatAnalyzer beatAnalyzer) {
//        showDialog("Downloaded " + result + " bytes");
        TextView textView = (TextView) ((Activity) context).findViewById(R.id.textView);

        textView.setText("Median BPM: " + beatAnalyzer.getMedianBPM());

        textView.append("\n\nBPM over time:");
        for(double bpm : beatAnalyzer.getBPMOverTime()) {
            textView.append("\n" + String.valueOf(bpm));
        }
    }
}
