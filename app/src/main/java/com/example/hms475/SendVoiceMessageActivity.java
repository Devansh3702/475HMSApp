//Team members: Devansh Shah, Jaishil Bhavsar, and Het Patel
package com.example.hms475;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;


public class SendVoiceMessageActivity extends AppCompatActivity {
    private Button recordBtn, stopBtn, playBtn;
    private Button sendButton;

    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    boolean isRecording = false;

    boolean messageRecordingDone = false;
    boolean helper = false;

    // for data persistence
    private static final String MESSAGE_RECORDING_DONE_KEY = "messageRecordingDone";
    private static final String HELPER_KEY = "helper";
    private static final String IS_RECORDING_KEY = "isRecording";
    private static final String RECORDING_FILE_PATH_KEY = "recordingFilePath";
    private String recordingFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendvoicemessage);

        // link buttons
        recordBtn = findViewById(R.id.button);
        stopBtn = findViewById(R.id.button2);
        playBtn = findViewById(R.id.button3);
        sendButton = findViewById(R.id.button4);
        sendButton.setEnabled(true);

        // Disable buttons initially
        setButtonsEnabled(false);

        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

        // Set button listeners
        setButtonListeners();

        sendButton.setOnClickListener(view -> {
            if(messageRecordingDone){
                Toast.makeText(SendVoiceMessageActivity.this, "Voice Message Sent!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SendVoiceMessageActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                // Empty subject or message field
                Toast.makeText(SendVoiceMessageActivity.this, "Please Record a Message first", Toast.LENGTH_SHORT).show();
            }
        });

        // for data persistence
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        } else {
            recordingFilePath = getRecordingFilePath();
        }
    }

    private boolean isMicrophonePresent() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        } else {
            setButtonsEnabled(true);
        }
    }

    private void setButtonListeners() {
        recordBtn.setOnClickListener(view -> {
            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setOutputFile(getRecordingFilePath());
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setOnInfoListener((mr, what, extra) -> {
                    Log.d("MediaRecorderInfo", "Info: " + what + ", Extra: " + extra);
                });
                mediaRecorder.prepare();
                mediaRecorder.start();

                isRecording = true;
                Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
                helper = true;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });


        stopBtn.setOnClickListener(view -> {
            if(isRecording){
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
                if(helper){
                    messageRecordingDone = true;
                }
            }

        });

        playBtn.setOnClickListener(view -> {
            try {
                Toast.makeText(this, "Recording is playing", Toast.LENGTH_SHORT).show();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getRecordingFilePath());
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });

                mediaPlayer.setOnCompletionListener(mp -> Toast.makeText(this, "Recording playing complete", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private String getRecordingFilePath() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File musicDirectory = cw.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile" + ".mp4");
        return file.getPath();
    }

    private void setButtonsEnabled(boolean enabled) {
        recordBtn.setEnabled(enabled);
        stopBtn.setEnabled(enabled);
        playBtn.setEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MICROPHONE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Microphone permission granted", Toast.LENGTH_SHORT).show();
                setButtonsEnabled(true);
            } else {
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putBoolean(MESSAGE_RECORDING_DONE_KEY, messageRecordingDone);
        outState.putBoolean(HELPER_KEY, helper);
        outState.putBoolean(IS_RECORDING_KEY, isRecording);
        outState.putString(RECORDING_FILE_PATH_KEY, recordingFilePath);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        messageRecordingDone = savedInstanceState.getBoolean(MESSAGE_RECORDING_DONE_KEY);
        helper = savedInstanceState.getBoolean(HELPER_KEY);
        isRecording = savedInstanceState.getBoolean(IS_RECORDING_KEY);
        recordingFilePath = savedInstanceState.getString(RECORDING_FILE_PATH_KEY);
    }

}
