package com.example.hms475.db;

import android.app.Application;

import java.util.List;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PatientViewModel extends AndroidViewModel {
    private LiveData<List<Patient>> patients;

    public PatientViewModel(Application application) {
        super(application);
        patients = PatientDatabase.getDatabase(getApplication()).patientDAO().getAll();
    }

    public LiveData<List<Patient>> getAllPatients() {
        return patients;
    }
}

