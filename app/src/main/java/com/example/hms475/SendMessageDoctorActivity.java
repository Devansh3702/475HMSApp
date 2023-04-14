package com.example.hms475;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hms475.db.Doctor;
import com.example.hms475.db.DoctorDatabase;

import java.util.List;


public class SendMessageDoctorActivity extends AppCompatActivity {

    private EditText subjectEditText;
    private EditText messageEditText;
    private Button sendButton;

    private RecyclerView recyclerView;

    private DoctorDatabase doctorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessagedoctor);

        subjectEditText = findViewById(R.id.subject_input);
        messageEditText = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // Initialize the doctor database
        doctorDatabase = DoctorDatabase.getDatabase(this);

        recyclerView = findViewById(R.id.doctors_list);
        DoctorListAdapter adapter = new DoctorListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe changes to the doctor list
        doctorDatabase.doctorDAO().getAll().observe(this, doctors -> {
            // Update the adapter with the new doctor list
            adapter.setDoctors(doctors);
            adapter.notifyDataSetChanged();
        });


        sendButton.setOnClickListener(view -> {
            // Get the selected doctor
            DoctorListAdapter adapter1 = (DoctorListAdapter) recyclerView.getAdapter();
            Doctor selectedDoctor = adapter1.getSelectedDoctor();

            if (selectedDoctor == null) {
                Toast.makeText(SendMessageDoctorActivity.this, "Please select a doctor", Toast.LENGTH_SHORT).show();
                return;
            }


            // Get the subject and message text
            String subject = subjectEditText.getText().toString().trim();
            String message = messageEditText.getText().toString().trim();

            // Check if the subject and message are not empty
            if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(message)) {
                // Send the message to the selected doctor
                Toast.makeText(SendMessageDoctorActivity.this, "Message sent to Doctor " + selectedDoctor.fullname, Toast.LENGTH_SHORT).show();
                // redirect to HomeActivity again
                Intent intent = new Intent(SendMessageDoctorActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Empty subject or message field
                Toast.makeText(SendMessageDoctorActivity.this, "Please enter subject and message", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder>{


        class DoctorViewHolder extends RecyclerView.ViewHolder {
            private final TextView fullname;

            private Doctor doctor;

            private DoctorViewHolder(View itemView) {
                super(itemView);
                fullname = itemView.findViewById(R.id.txtTitle);

                itemView.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Doctor selectedDoctor = doctors.get(position);
                        setSelectedDoctor(selectedDoctor);
                    }
                });
            }
        }

        private final LayoutInflater layoutInflater;
        private List<Doctor> doctors; // Cached copy of doctors
        private Doctor selectedDoctor;

        DoctorListAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new DoctorViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DoctorViewHolder holder, int position) {
            if (doctors != null) {
                Doctor current = doctors.get(position);
                holder.doctor = current;
                holder.fullname.setText(current.fullname);
            } else {
                // Covers the case of data not being ready yet.
                holder.fullname.setText("No doctors available");
            }
        }

        void setDoctors(List<Doctor> doctors){
            this.doctors = doctors;
            notifyDataSetChanged();
        }

        void setSelectedDoctor(Doctor doctor) {
            selectedDoctor = doctor;
            notifyDataSetChanged();
        }

        Doctor getSelectedDoctor() {
            return selectedDoctor;
        }

        @Override
        public int getItemCount() {
            if (doctors != null) {
                return doctors.size();
            } else {
                return 0;
            }
        }
    }
}