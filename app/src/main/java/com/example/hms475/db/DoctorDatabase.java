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

@Database(entities = {Doctor.class}, version = 1, exportSchema = false)
public abstract class DoctorDatabase extends RoomDatabase {

    public interface DoctorListener {
        void onDoctorReturned(Doctor patient);
    }

    public abstract DoctorDAO doctorDAO();

    private static DoctorDatabase INSTANCE;

    public static DoctorDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DoctorDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DoctorDatabase.class, "doctor_database")
                            .addCallback(createDoctorDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createDoctorDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    createDoctorTable();
                }
            };

    private static void createDoctorTable() {
        for (int i = 0; i < DefaultContent.FULLNAME.length; i++) {
            insert(new Doctor(0, DefaultContent.FULLNAME[i]));
        }
    }

    public static void getDoctor(int id, DoctorDatabase.DoctorListener listener) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listener.onDoctorReturned((Doctor) msg.obj);
            }
        };

        (new Thread(() -> {
            Message msg = handler.obtainMessage();
            msg.obj = INSTANCE.doctorDAO().getById(id);
            handler.sendMessage(msg);
        })).start();
    }

    public static void insert(Doctor doctor) {
        (new Thread(() -> INSTANCE.doctorDAO().insert(doctor))).start();
    }

    public static void delete(int id) {
        (new Thread(() -> INSTANCE.doctorDAO().delete(id))).start();
    }


    public static void update(Doctor doctor) {
        (new Thread(() -> INSTANCE.doctorDAO().update(doctor))).start();
    }
}

