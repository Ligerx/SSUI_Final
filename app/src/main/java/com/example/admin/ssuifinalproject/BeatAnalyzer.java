package com.example.admin.ssuifinalproject;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

public class BeatAnalyzer {

    private static final String TAG = "BeatAnalyzer";

    private final String wavFilePath;
    private final Context context;

    private BeatData beatData;

    public BeatAnalyzer(String wavFilePath, Context context) {
        this.wavFilePath = wavFilePath;
        this.context = context;

        ArrayList<Double> beatTiming = findBeatTiming();
        this.beatData = new BeatData(beatTiming);
    }

    private ArrayList<Double> findBeatTiming() {
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
        final ArrayList<Double> beatTiming = new ArrayList<>();
        handler.trackBeats(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                Log.d(TAG, String.valueOf(time));
                beatTiming.add(time); // Time is recorded here
            }
        });

        return beatTiming;
    }


    // BeatData getter
    public BeatData getBeatData() {
        return beatData;
    }
}
