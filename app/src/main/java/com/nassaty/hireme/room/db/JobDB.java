package com.nassaty.hireme.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nassaty.hireme.model.Job;

@Database(entities = Job.class, version = 1)
public abstract class JobDB extends RoomDatabase {

    private static JobDB instance;

    public static JobDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), JobDB.class, "notes_db")
                    .build();
        }

        return instance;
    }

    public static void detachRoom(){instance = null;}

    public abstract JobDao getDB();

}
