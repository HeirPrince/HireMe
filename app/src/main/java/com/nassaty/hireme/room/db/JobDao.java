package com.nassaty.hireme.room.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nassaty.hireme.model.Job;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface JobDao {

    @Query("SELECT * FROM Job")
    LiveData<List<Job>> getAllNotes();

    @Query("SELECT * FROM Job WHERE id = :id")
    Job getJobById(String id);

    @Insert(onConflict = REPLACE)
    void insertJob(Job job);

    @Delete
    void deleteJob(Job job);



}
