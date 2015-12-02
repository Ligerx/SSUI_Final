package com.example.admin.ssuifinalproject;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

// FIXME can I nudge the BPM towards the right #? Right now it sometimes is a multiple of the original.
public class BeatAnalyzer {

    private static final String TAG = "BeatAnalyzer";

    private final String wavFilePath;
    private final Context context;

    private final ArrayList<Double> beatTiming = new ArrayList<>();
    private final ArrayList<Double> beatIntervals;

    public BeatAnalyzer(String wavFilePath, Context context) {
        this.wavFilePath = wavFilePath;
        this.context = context;

        findBeatTiming();
        this.beatIntervals = findBeatIntervals();
    }

    private void findBeatTiming() {
        Log.d(TAG, "findBeatTiming() should be running");

        new AndroidFFMPEGLocator(context); // gets the FFMEG decoder for TarsosDSP

        int rate = 44100;
        int size = 512;
        int overlap = 256;
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(wavFilePath, rate, size, overlap);

        ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
        BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
        detector.setHandler(handler);

        dispatcher.addAudioProcessor(detector);
        dispatcher.run();

        // The beat detector will call handleOnset for every beat it detects
        handler.trackBeats(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                Log.d(TAG, String.valueOf(time));
                beatTiming.add(time); // Time is recorded here
            }
        });
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


    //// Standard Getters
    public ArrayList<Double> getBeatTiming() {
        return beatTiming;
    }

    public ArrayList<Double> getBeatIntervals() {
        return beatIntervals;
    }
}
