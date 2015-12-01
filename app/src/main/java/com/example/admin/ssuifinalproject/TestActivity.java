package com.example.admin.ssuifinalproject;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

public class TestActivity extends AppCompatActivity {

    private Button recordButton;
    private MediaRecorder recorder;
    private MediaPlayer player;

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

                if(isRecording) {
                    recordButton.setText(waitingText);
                    stopRecording();
                    // TODO analyze the recorded file
                }
                else {
                    recordButton.setText(recordingText);
                    startRecording();
                }
            }
        });
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "startRecording prepare() failed");
        }

        recorder.start();
    }

    private String fileName() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        // Right now the file name is based on a time stamp
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        String timeStamp = formatter.format(new Date());

        String fileExtension = ".3gp";

        savedFileName = path + timeStamp + fileExtension;
        return path + timeStamp + fileExtension;
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void analyzeBeat() {
//        File audioFile = new File(savedFileName);
        int rate = 44100;
        int size = 512;
        int overlap = 256;
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(savedFileName, rate, size, overlap);

        ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
        BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
        detector.setHandler(handler);

        handler.trackBeats(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                Log.d(LOG_TAG, String.valueOf(time));
            }
        });
    }

//    public boolean run(String... args) throws UnsupportedAudioFileException, IOException {
//        String inputFile = args[1];
//        File audioFile = new File(inputFile);
//        int rate = 44100;
//        int size = 512;
//        int overlap = 256;
//        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, rate, size, overlap);
//
//        ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
//        BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
//        detector.setHandler(handler);
//
//        dispatcher.addAudioProcessor(detector);
//        dispatcher.run();
//
//        handler.trackBeats(this);
//
//        return true;
//    }
//
//    @Override
//    public void handleOnset(double time, double salience) {
//        System.out.println(time);
//    }
}
