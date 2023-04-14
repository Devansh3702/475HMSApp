package com.example.hms475.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Doctors")
public class Doctor {

    public Doctor(int id, String fullname)
    {
        this.id = id;
        this.fullname = fullname;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "doctorID")
    public int id;

    @ColumnInfo(name = "fullname")
    public String fullname;
}
