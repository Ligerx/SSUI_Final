package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.BPMAsyncTask;
import com.example.admin.ssuifinalproject.ExtAudioRecorder;
import com.example.admin.ssuifinalproject.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordPracticeRunActivity extends AppCompatActivity {

    String TAG = "RecordPracticeRunActivity";

    int song_id;
    int targetBPM;

    ImageView micImage;
    ProgressBar processingSpinner;
    TextView recordText;

    boolean isRecording = false;

    // alternate recorder class for .wav files
    private ExtAudioRecorder extAudioRecorder;
    String savedFileName; // path to the just recorded audio file


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_practice_run);

        Intent intent = getIntent();
        song_id = intent.getIntExtra("song_id", -1);
        targetBPM = intent.getIntExtra("targetBPM", -1);

        micImage = (ImageView) findViewById(R.id.micImage);
        processingSpinner = (ProgressBar) findViewById(R.id.processingSpinner);
        recordText = (TextView) findViewById(R.id.recordText);

        micImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMic();
            }
        });
    }

    private void clickMic() {
        if(!isRecording) {
            isRecording = true;
            recordText.setText(R.string.isRecording);
            micImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_black_48dp));

            startRecording();
        }
        else {
            recordText.setText(R.string.processingRecording);

            // replace mic with spinner
            micImage.setVisibility(View.GONE);
            processingSpinner.setVisibility(View.VISIBLE);

            stopRecording();
            processAudio();
        }
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

    private void processAudio() {
        new BPMAsyncTask(this, song_id, targetBPM).execute(savedFileName);
    }
}
