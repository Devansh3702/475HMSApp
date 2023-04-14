package com.example.hms475;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hms475.db.Doctor;
import com.example.hms475.db.DoctorDatabase;

import java.util.List;


public class SendMessageDoctorActivity extends AppCompatActivity {

    // private Spinner doctorsSpinner;
    private EditText subjectEditText;
    private EditText messageEditText;
    private Button sendButton;

    private RecyclerView recyclerView;

    private DoctorDatabase doctorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessagedoctor);

        // doctorsSpinner = findViewById(R.id.recipient_spinner);
        subjectEditText = findViewById(R.id.subject_input);
        messageEditText = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);


        // Initialize the doctor database
        doctorDatabase = DoctorDatabase.getDatabase(this);

        // TODO: implement a recyclerview that helps display the doctor names in a dropdown list. Also implement a DoctorListAdapter that extends RecyclerView.Adapter with a RecyclerView.ViewHolder
        // Initialize the doctor list adapter and set it to the recycler view
        recyclerView = findViewById(R.id.doctors_list);
        DoctorListAdapter adapter = new DoctorListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe changes to the doctor list
        doctorDatabase.doctorDAO().getAll().observe(this, new Observer<List<Doctor>>() {
            @Override
            public void onChanged(List<Doctor> doctors) {
                // Update the adapter with the new doctor list
                adapter.setDoctors(doctors);
                adapter.notifyDataSetChanged();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected doctor
                DoctorListAdapter adapter = (DoctorListAdapter) recyclerView.getAdapter();
                Doctor selectedDoctor = adapter.getSelectedDoctor();

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
                    Toast.makeText(SendMessageDoctorActivity.this, "Message sent to " + selectedDoctor.fullname, Toast.LENGTH_SHORT).show();
                } else {
                    // Empty subject or message field
                    Toast.makeText(SendMessageDoctorActivity.this, "Please enter subject and message", Toast.LENGTH_SHORT).show();
                }
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