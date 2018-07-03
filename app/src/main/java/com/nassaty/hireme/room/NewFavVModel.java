package com.nassaty.hireme.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.nassaty.hireme.room.db.FavoritesDB;
import com.nassaty.hireme.model.Job;

public class NewFavVModel extends AndroidViewModel {

    private FavoritesDB favoritesDB;

    public NewFavVModel(@NonNull Application application) {
        super(application);
        favoritesDB = FavoritesDB.getInstance(this.getApplication());
    }

    public void addFavorites(Job job){
        new favAsyncTask(favoritesDB).execute(job);
    }

    public class favAsyncTask extends AsyncTask<Job, Void, Void>{
        private FavoritesDB favoritesDB;

        public favAsyncTask(FavoritesDB favoritesDB){
            this.favoritesDB = favoritesDB;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            favoritesDB.getFavorites().addToFavorites(jobs[0]);
            return null;
        }
    }

}
