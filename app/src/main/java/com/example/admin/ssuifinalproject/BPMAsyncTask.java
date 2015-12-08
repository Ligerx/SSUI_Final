package com.example.admin.ssuifinalproject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class BPMAsyncTask extends AsyncTask<String, Void, BeatAnalyzer> {

    private Context context;
    private LinearLayout loading;
    private LinearLayout resultsContainer;
    private Chart chart;

    public BPMAsyncTask(Context context) {
        this.context = context;
        this.loading = (LinearLayout) ((Activity) context).findViewById(R.id.loading_placeholder);
        this.resultsContainer = (LinearLayout) ((Activity) context).findViewById(R.id.results_container);
        this.chart = (Chart) ((Activity) context).findViewById(R.id.chart);
    }

    protected BeatAnalyzer doInBackground(String... args) {
        // Should be path to audio file
        String path = args[0];
        return new BeatAnalyzer(path, context);
    }

    protected void onPostExecute(BeatAnalyzer beatAnalyzer) {
        loading.setVisibility(View.GONE); // hide the loading spinner
        resultsContainer.setVisibility(View.VISIBLE); // show everything else

        ArrayList<Double> bpm = beatAnalyzer.getBeatData().getBPMOverTime();


        ArrayList<Entry> bpmEntries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        // Add all bpm data points, time independent right now
        for(int i = 0; i < bpm.size(); i++) {
            Entry entry = new Entry(bpm.get(i).floatValue(), i);
            bpmEntries.add(entry);

            xVals.add(String.valueOf(i));
        }

        LineDataSet bpmLine = new LineDataSet(bpmEntries, "BPM (no time yet)");
        bpmLine.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(bpmLine);

        LineData lineChart = new LineData(xVals, dataSets);

        chart.setData(lineChart);
        chart.invalidate(); // (re)draw
    }
}
