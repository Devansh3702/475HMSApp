package com.example.hms475.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DoctorDAO {

    @Query("SELECT * FROM Doctors ORDER BY fullname COLLATE NOCASE, doctorID")
    LiveData<List<Doctor>> getAll();

    @Query("SELECT * FROM Doctors WHERE doctorID = :doctorId")
    Doctor getById(int doctorId);

    @Insert
    void insert(Doctor... doctor);

    @Update
    void update(Doctor... doctors);

    @Delete
    void delete(Doctor... doctor);

    @Query("DELETE FROM Doctors WHERE doctorID = :doctorId")
    void delete(int doctorId);

}

