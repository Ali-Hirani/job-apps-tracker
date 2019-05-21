package com.ahirani.jobappstracker.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {JobApp.class}, version = 1, exportSchema = false)
public abstract class JobAppDatabase extends RoomDatabase {
    private static JobAppDatabase jobAppDb;

    public abstract JobAppDao jobAppDao();

    public static JobAppDatabase getInstance(Context context) {
        if (jobAppDb == null) {
            return Room.databaseBuilder(context.getApplicationContext(), JobAppDatabase.class, "job-app-database").build();
        } else {
            return jobAppDb;
        }
    }
}
