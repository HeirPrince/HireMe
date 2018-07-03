package com.nassaty.hireme.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nassaty.hireme.model.Job;

@Database(entities = Job.class, version = 1)
public abstract class FavoritesDB extends RoomDatabase {

    private static FavoritesDB instance;

    public static FavoritesDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoritesDB.class, "favorite_jobs_db").build();
        return instance;

    }

    public static void detachFavorites(){
        instance = null;
    }

    public abstract FavoriteDao getFavorites();
}
