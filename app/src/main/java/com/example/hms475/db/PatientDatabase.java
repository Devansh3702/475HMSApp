package com.example.hms475.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hms475.MainActivity;

import java.util.logging.LogRecord;

@Database(entities = {Patient.class}, version = 1, exportSchema = false)
public abstract class PatientDatabase extends RoomDatabase {



    public interface PatientListener {
        void onPatientReturned(Patient patient);
    }

    public abstract PatientDAO patientDAO();

    private static PatientDatabase INSTANCE;

    public static PatientDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PatientDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PatientDatabase.class, "patient_database")
                            .addCallback(createPatientDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createPatientDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    createPatientTable();
                }
            };

    private static void createPatientTable() {
        for (int i = 0; i < DefaultContent.FIRSTNAME.length; i++) {
            insert(new Patient(0, DefaultContent.FIRSTNAME[i], DefaultContent.LASTNAME[i],
                    DefaultContent.ADDRESS[i], DefaultContent.SSN[i], DefaultContent.PHONE[i],
                    DefaultContent.INSURANCEID[i], DefaultContent.PCP[i], DefaultContent.USERNAME[i], DefaultContent.PASSWORD[i]));
        }
    }

    public static void getPatient(int id, PatientListener listener) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listener.onPatientReturned((Patient) msg.obj);
            }
        };

        (new Thread(() -> {
            Message msg = handler.obtainMessage();
            msg.obj = INSTANCE.patientDAO().getById(id);
            handler.sendMessage(msg);
        })).start();
    }

    public static void insert(Patient patient) {
        (new Thread(() -> INSTANCE.patientDAO().insert(patient))).start();
    }

    public static void delete(int id) {
        (new Thread(() -> INSTANCE.patientDAO().delete(id))).start();
    }


    public static void update(Patient patient) {
        (new Thread(() -> INSTANCE.patientDAO().update(patient))).start();
    }



}


