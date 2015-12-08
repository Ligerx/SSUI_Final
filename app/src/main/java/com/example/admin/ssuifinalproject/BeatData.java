package com.example.admin.ssuifinalproject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class BeatData {
    private final String TAG = "BeatData";

    private final ArrayList<Double> beatTiming;
    private final ArrayList<Double> beatIntervals;

    // FIXME can I nudge the BPM towards the right #? Right now it sometimes is a multiple of the original.
    public BeatData(ArrayList<Double> beatTiming) {
        this.beatTiming = beatTiming;
        this.beatIntervals = findBeatIntervals();
    }

    private ArrayList<Double> findBeatIntervals() {
        // Calculate the difference between the beat times
        ArrayList<Double> intervals = new ArrayList<>();

        // skip the first value, it doesn't have an interval to compare
        for(int i = 1; i < beatTiming.size(); i++) {
            double previous = beatTiming.get(i - 1);
            double current = beatTiming.get(i);

            double difference = current - previous;
            Log.d(TAG, String.valueOf(difference));

            intervals.add(difference);
        }

        return intervals;
    }

    private double findMedianValue(ArrayList<Double> values) {
        ArrayList<Double> sortedIntervals = new ArrayList<>(values); // copy because why not
        Collections.sort(sortedIntervals); // in place sort

        return median(sortedIntervals);
    }

    private double median(ArrayList<Double> m) {
        int middle = m.size()/2;
        if (m.size()%2 == 1) {
            return m.get(middle);
        } else {
            return (m.get(middle-1) + m.get(middle)) / 2.0;
        }
    }

    public double getMedianBPM() {
        return timeToBPM(findMedianValue(beatIntervals));
    }

    // TODO consider how to throw away extremely long intervals (bad data)
    // and what about extremely short intervals too?
    public ArrayList<Double> getBPMOverTime() {
        ArrayList<Double> bpm = new ArrayList<>();
        for(double interval : beatIntervals) {
            bpm.add(timeToBPM(interval));
        }

        return bpm;
    }

    private double timeToBPM(double seconds) {
        // x (bpm) = 1 (beat) / minute (60 seconds)
        // x = 1 * 60sec / seconds
        return 60.0 / seconds;
    }


    // Standard Getters and Setters
    public ArrayList<Double> getBeatTiming() {
        return beatTiming;
    }

    public ArrayList<Double> getBeatIntervals() {
        return beatIntervals;
    }
}
