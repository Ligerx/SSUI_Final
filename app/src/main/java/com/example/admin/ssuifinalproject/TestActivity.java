package com.example.admin.ssuifinalproject;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

public class TestActivity extends AppCompatActivity {

    private Button recordButton;
    private MediaRecorder recorder;
    private MediaPlayer player;

    // alternate recorder class for .wav files
    private ExtAudioRecorder extAudioRecorder;

    private final String waitingText = "Record";
    private final String recordingText = "Stop";
    private boolean isRecording = false;

    private static final String LOG_TAG = "TestActivity";

    String savedFileName; // path to the just recorded audio file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recordButton = (Button) findViewById(R.id.record);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Clicked record button");

                if (isRecording) {
                    isRecording = false;
                    recordButton.setText(waitingText);
                    stopRecording();
                    analyzeBeat();
                } else {
                    isRecording = true;
                    recordButton.setText(recordingText);
                    startRecording();
                }
            }
        });
    }

    private void startRecording() {
        //// USING ExtAudioRecorder for .wav files
        // Start recording
//        extAudioRecorder = ExtAudioRecorder.getInstance(true);	  // Compressed recording (AMR)
        extAudioRecorder = ExtAudioRecorder.getInstance(false); // Uncompressed recording (WAV)

        extAudioRecorder.setOutputFile(fileName());
        extAudioRecorder.prepare();
        extAudioRecorder.start();
    }

    private String fileName() {
        String externalDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        String appFolderName = "SSUI";
        String fullDirectory = externalDirectory + File.separator + appFolderName;

        // Right now the file name is based on a time stamp
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        String timeStamp = formatter.format(new Date());
//        String fileExtension = ".3gp";
        String fileExtension = ".wav"; // .wav with extRecorder
        String fileName = timeStamp + fileExtension;

        // If the app folder doesn't exist, create it
        File folder = new File(fullDirectory);
        if (folder.mkdir() || folder.isDirectory()) {
            // successfully created directory
        }
        else {
            Log.d(LOG_TAG, "fileName() failed to create folder");
        }

        String directoryAndFile = fullDirectory + File.separator + fileName;

        Log.d(LOG_TAG, directoryAndFile);
        savedFileName = directoryAndFile; // temp hard-code-saving it here

        return directoryAndFile;
    }

    private void stopRecording() {
        // EXT RECORDER CODE
        // Stop recording
        extAudioRecorder.stop();
        extAudioRecorder.release();
        extAudioRecorder = null;
    }

    private void analyzeBeat() {
        Log.d(LOG_TAG, "analyzeBeat() should be running");

        new AndroidFFMPEGLocator(this); // gets the FFMEG decoder for TarsosDSP

        int rate = 44100;
        int size = 512;
        int overlap = 256;
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(savedFileName, rate, size, overlap);

        ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
        BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
        detector.setHandler(handler);

        dispatcher.addAudioProcessor(detector);
        dispatcher.run();

        final ArrayList<Double> times = new ArrayList<Double>();

        handler.trackBeats(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                Log.d(LOG_TAG, String.valueOf(time));
                times.add(time);
            }
        });

        calculateBPM(times);
    }

    private void calculateBPM(ArrayList<Double> times) {
        // Calculate the difference between the beat times
        ArrayList<Double> intervals = new ArrayList<>();
        for(int i = 1; i < times.size(); i++) { // skip the first value
            double previous = times.get(i - 1);
            double current = times.get(i);

            double difference = current - previous;
            Log.d(LOG_TAG, String.valueOf(difference));

            intervals.add(difference);
        }

        // Calculate median bpm I guess
        ArrayList<Double> sortedIntervals = new ArrayList<>(intervals); // copy because why not
        Collections.sort(sortedIntervals); // in place sort

        double medianInterval = median(sortedIntervals);
        Log.d(LOG_TAG, "Median interval: " + String.valueOf(medianInterval));

        Log.d(LOG_TAG, "Median BPM: " + String.valueOf(timeToBPM(medianInterval)));
    }

    private double median(ArrayList<Double> m) {
        int middle = m.size()/2;
        if (m.size()%2 == 1) {
            return m.get(middle);
        } else {
            return (m.get(middle-1) + m.get(middle)) / 2.0;
        }
    }

    private double timeToBPM(double seconds) {
        // x (bpm) = 1 (beat) / minute (60 seconds)
        // x = 1 * 60sec / seconds
        return 60.0 / seconds;
    }
}
