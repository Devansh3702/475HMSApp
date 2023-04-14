package com.example.hms475;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    // temp variables
    private Button scheduleButton;
    private Button medicalReportsButton;
    private Button labReportsButton;
    private Button sendMessageButton;
    private Button medicationsButton;
    private Button referralButton;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // temporary linking
        scheduleButton = findViewById(R.id.schedule_button);
        medicalReportsButton = findViewById(R.id.medical_reports_button);
        labReportsButton = findViewById(R.id.lab_reports_button);
        sendMessageButton = findViewById(R.id.send_message_button);
        medicationsButton = findViewById(R.id.medications_button);
        referralButton = findViewById(R.id.referral_button);
        signOutButton = findViewById(R.id.sign_out_button);


        scheduleButton.setOnClickListener(this);
        medicalReportsButton.setOnClickListener(this);
        labReportsButton.setOnClickListener(this);
        sendMessageButton.setOnClickListener(this);
        medicationsButton.setOnClickListener(this);
        referralButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schedule_button:
                Intent intent = new Intent(HomeActivity.this, ScheduleAppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.medical_reports_button:
                // TODO: Implement View Medical Reports functionality here
                break;
            case R.id.lab_reports_button:
                // TODO: Implement View Lab Reports functionality here
                break;
            case R.id.send_message_button:
                // TODO: Implement Send Message to Doctor functionality here
                break;
            case R.id.medications_button:
                // TODO: Implement See my Medications functionality here
                break;
            case R.id.referral_button:
                // TODO: Implement Get Referral functionality here
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
