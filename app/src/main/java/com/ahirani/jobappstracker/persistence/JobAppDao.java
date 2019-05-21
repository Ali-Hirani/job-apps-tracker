package com.ahirani.jobappstracker.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface JobAppDao {
    @Query("SELECT * FROM job_app")
    Flowable<List<JobApp>> getAllJobApps();

    @Insert
    Single<Long> insert(JobApp app);

    @Delete
    Completable delete(JobApp... jobApps);
}
