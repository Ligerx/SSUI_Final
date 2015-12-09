package com.example.admin.ssuifinalproject;

import android.graphics.Color;
import android.os.AsyncTask;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class AsyncChartSetter extends AsyncTask<BeatData, Void, BeatData> {

    LineChart chart;
    double targetBPM;

    public AsyncChartSetter(Chart chart, double targetBPM) {
        this.chart = (LineChart) chart;
        this.targetBPM = targetBPM;
    }

    @Override
    protected BeatData doInBackground(BeatData... params) {
        return params[0];
    }

    @Override
    protected void onPostExecute(BeatData beatData) {
        super.onPostExecute(beatData);

        ArrayList<Double> bpm = beatData.getBPMOverTime();

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


        // Limit line for target bpm
        XAxis xaxis = chart.getXAxis();
        LimitLine ll = new LimitLine((float) targetBPM, "Target BPM");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLACK);
        xaxis.addLimitLine(ll);

        chart.invalidate(); // (re)draw
    }
}
