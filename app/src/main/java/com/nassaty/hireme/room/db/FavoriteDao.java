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
public interface FavoriteDao {

    @Insert(onConflict = REPLACE)
    void addToFavorites(Job job);

    @Delete
    void deletefromFavorites(Job job);

    @Query("SELECT * FROM Job")
    LiveData<List<Job>> getAllFavorites();

}
