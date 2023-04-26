//Team Members - Jaishil Bhavsar, Devansh Shah, Het Patel
//CMPSC 475 Final Project
package com.example.hms475;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

// no data to persist in this activity.
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private Button scheduleButton;
    private Button medicalReportsButton;
    private Button schedulelabButton;
    private Button sendMessageButton;
    private Button medicationsButton;
    private Button referralButton;
    private Button signOutButton;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set default values in shared preferences if they have never been set
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setTheme();

        scheduleButton = findViewById(R.id.schedule_button);
        schedulelabButton = findViewById(R.id.lab_reports_button);
        sendMessageButton = findViewById(R.id.send_message_button);
        medicationsButton = findViewById(R.id.medications_button);
        signOutButton = findViewById(R.id.sign_out_button);


        scheduleButton.setOnClickListener(this);

        schedulelabButton.setOnClickListener(this);
        sendMessageButton.setOnClickListener(this);
        medicationsButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

    }

    private void setTheme() {
        if (sharedPreferences.getBoolean("tfSetting", true)) {
            // Light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            // Dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if theme mode has been changed
        boolean isLightMode = sharedPreferences.getBoolean("tfSetting", true);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (isLightMode && currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            recreate();
        } else if (!isLightMode && currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            recreate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schedule_button:
                Intent intent = new Intent(HomeActivity.this, ScheduleAppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.lab_reports_button:
                // redirect to ScheduleLabAppointment.java
                Intent intent4 = new Intent(HomeActivity.this, ScheduleLabAppointment.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.send_message_button:
                // redirect to SendMessageDoctorActivity.java
                Intent intent3 = new Intent(HomeActivity.this, SendMessageDoctorActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.medications_button:
                Intent i2 = new Intent(HomeActivity.this, SendVoiceMessageActivity.class);
                startActivity(i2);
                finish();
                break;
            case R.id.sign_out_button:
                // redirect to MainActivity again (login page)
                Intent intent2 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
                break;

        }
    }
}
