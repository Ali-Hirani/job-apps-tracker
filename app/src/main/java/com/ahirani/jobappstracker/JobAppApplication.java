package com.ahirani.jobappstracker;

import android.app.Application;

import com.ahirani.jobappstracker.persistence.JobAppDatabase;
import com.facebook.stetho.Stetho;

public class JobAppApplication extends Application {
    public JobAppDatabase mDatabase;

    public JobAppDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        mDatabase = JobAppDatabase.getInstance(this);
    }
}
