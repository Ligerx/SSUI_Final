package com.example.admin.ssuifinalproject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;

public class BPMAsyncTask extends AsyncTask<String, Void, BeatAnalyzer> {

    private Context context;
    private Chart chart;
    private LinearLayout loading;
//    private TextView textView;

    public BPMAsyncTask(Context context) {
        this.context = context;
        this.chart = (Chart) ((Activity) context).findViewById(R.id.chart);
        this.loading = (LinearLayout) ((Activity) context).findViewById(R.id.loading_placeholder);
//        this.textView = (TextView) ((Activity) context).findViewById(R.id.textView);
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        textView.setText("Processing...");
//    }

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

//        textView.setText("Median BPM: " + beatAnalyzer.getMedianBPM());
//
//        textView.append("\n\nBPM over time:");
//        for(double bpm : beatAnalyzer.getBPMOverTime()) {
//            textView.append("\n" + String.valueOf(bpm));
//        }

        loading.setVisibility(View.GONE); // hide the loading spinner
        chart.setVisibility(View.VISIBLE); // show the chart


    }
}
