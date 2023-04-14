package com.example.hms475.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DoctorViewModel extends AndroidViewModel {

    private LiveData<List<Doctor>> doctors;

    public DoctorViewModel(Application application) {
        super(application);
        doctors = DoctorDatabase.getDatabase(getApplication()).doctorDAO().getAll();
    }

    public LiveData<List<Doctor>> getAllDoctors() {
        return doctors;
    }
}
