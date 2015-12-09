package com.example.admin.ssuifinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.ssuifinalproject.R;

public class RecordPracticeRunActivity extends AppCompatActivity {

    int song_id;
    int targetBPM;

    ImageView micImage;
    ProgressBar processingSpinner;
    TextView recordText;

    boolean isRecording = false;

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
        }
        else {
            recordText.setText(R.string.processingRecording);

            // replace mic with spinner
            micImage.setVisibility(View.GONE);
            processingSpinner.setVisibility(View.VISIBLE);
        }

    }
}
