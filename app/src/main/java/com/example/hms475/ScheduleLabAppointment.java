package com.example.hms475;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ScheduleLabAppointment extends AppCompatActivity {

    private Spinner dateSpinner2;
    private Spinner timeSpinner2;
    private Button scheduleButton2;

    private static String TAG = "Connection Successful";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulelabappointment);

        dateSpinner2 = findViewById(R.id.date_spinner2);
        timeSpinner2 = findViewById(R.id.time_spinner2);
        scheduleButton2 = findViewById(R.id.schedule_button2);

        // Populate the date spinner with calendar dates
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(
                this, R.array.calendar_dates, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner2.setAdapter(dateAdapter);

        // Populate the time spinner with available appointment times
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                this, R.array.appointment_times, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner2.setAdapter(timeAdapter);

        // Retrieve lab test names from Firestore and populate in spinner
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("labtests").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Spinner labSpinner = findViewById(R.id.lab_spinner);
                    ArrayAdapter<String> labAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
                    labAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Map<String, Object> lab = queryDocumentSnapshots.getDocuments().get(i).getData();
                        for (String key : lab.keySet()) {
                            String value = (String) lab.get(key);
                            labAdapter.add(value);
                        }
                    }
                    labSpinner.setAdapter(labAdapter);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error retrieving lab tests from Firestore", e));


        // Set an onClickListener for the schedule button
        scheduleButton2.setOnClickListener(view -> {
            // Get the selected date and time from the spinners
            String selectedDate = dateSpinner2.getSelectedItem().toString();
            String selectedTime = timeSpinner2.getSelectedItem().toString();
            String selectedLabTest = ((Spinner)findViewById(R.id.lab_spinner)).getSelectedItem().toString();

            // Display a toast message with the selected date and time
            Toast.makeText(ScheduleLabAppointment.this,
                    "Lab appointment scheduled for " + selectedLabTest + " on " + selectedDate + " at " + selectedTime,
                    Toast.LENGTH_LONG).show();

            // redirect to HomeActivity again
            Intent intent = new Intent(ScheduleLabAppointment.this, HomeActivity.class);
            startActivity(intent);
            finish();

        });

        addDataToFirestore();

    }
    //firebase data
    private void addDataToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> lab = new HashMap<>();
        lab.put("1", "Lipid Profile");
        lab.put("2", "Basic Metabolic Panel (BMP)");
        lab.put("3", "Urine test");
        lab.put("4", "Bone Density test");
        lab.put("5", "Thyroid test");
        lab.put("6", "Allergy skin test");
        lab.put("7", "Antibody Sensitivity test");
        lab.put("8", "X-Ray");
        lab.put("9", "MRI Scan");
        lab.put("10", "Blood Glucose Test");

        // Add the lab tests data to Firestore
        db.collection("labtests")
                .add(lab)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}