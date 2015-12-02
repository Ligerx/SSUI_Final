package com.example.admin.ssuifinalproject;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    private Button recordButton;
    private MediaRecorder recorder;
    private MediaPlayer player;

    // alternate recorder class for .wav files
    private ExtAudioRecorder extAudioRecorder;

    private final String waitingText = "Record";
    private final String recordingText = "Stop";
    private boolean isRecording = false;

    private static final String TAG = "TestActivity";

    String savedFileName; // path to the just recorded audio file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        recordButton = (Button) findViewById(R.id.record);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked record button");

                if (isRecording) {
                    isRecording = false;
                    recordButton.setText(waitingText);
                    stopRecording();
                    showResults();
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
            Log.d(TAG, "fileName() failed to create folder");
        }

        String directoryAndFile = fullDirectory + File.separator + fileName;

        Log.d(TAG, directoryAndFile);
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

    private void showResults() {
        new BPMAsyncTask(this).execute(savedFileName);
    }
}
