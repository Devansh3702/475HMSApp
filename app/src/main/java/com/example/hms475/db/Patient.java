package com.example.hms475.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="patients")
public class Patient {

    public Patient(int id, String firstName, String lastName, String address, String SSN, String phone, String insuranceID, String PCP, String userName, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.SSN = SSN;
        this.phone = phone;
        this.insuranceID = insuranceID;
        this.PCP = PCP;
        this.userName = userName;
        this.password = password;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "patientID")
    public int id;

    @ColumnInfo(name = "firstName")
    public String firstName;

    @ColumnInfo(name = "lastName")
    public String lastName;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "SSN")
    public String SSN;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "insuranceID")
    public String insuranceID;

    @ColumnInfo(name = "PCP")
    public String PCP;

    @ColumnInfo(name = "userName")
    public String userName;

    @ColumnInfo(name = "password")
    public String password;
}
