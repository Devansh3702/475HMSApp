package com.example.hms475.db;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PatientDAO {

    @Query("SELECT * FROM patients ORDER BY firstName COLLATE NOCASE, patientID")
    LiveData<List<Patient>> getAll();

    @Query("SELECT * FROM patients WHERE patientID = :patientId")
    Patient getById(int patientId);

    @Insert
    void insert(Patient... patient);

    @Update
    void update(Patient... patients);

    @Delete
    void delete(Patient... patient);

    @Query("DELETE FROM patients WHERE patientID = :patientId")
    void delete(int patientId);

    // for loginActivity
    @Query("SELECT * FROM patients WHERE userName = :email")
    Patient getPatientByEmail(String email);
}

