package com.example.hms475;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hms475.db.Patient;
import com.example.hms475.db.PatientDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private PatientDatabase patientDatabase;

    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        patientDatabase = PatientDatabase.getDatabase(this);

        if (savedInstanceState != null) {
            userName = savedInstanceState.getString("email");
            password = savedInstanceState.getString("password");
            emailEditText.setText(userName);
            passwordEditText.setText(password);
        }


        loginButton.setOnClickListener(v -> {
            String userName = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            
            // Check if email and password are valid
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                // using another thread to access database
                new Thread(() -> {
                    Patient patient = patientDatabase.patientDAO().getPatientByEmail(userName);
                    if (patient != null && password.equals(patient.password)) {
                        // Successful login
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            } else {
                // Empty email or password fields
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", userName);
        outState.putString("password", password);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userName = savedInstanceState.getString("email");
        password = savedInstanceState.getString("password");
    }
}