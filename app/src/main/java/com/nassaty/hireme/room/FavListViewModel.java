package com.nassaty.hireme.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.nassaty.hireme.room.db.FavoritesDB;
import com.nassaty.hireme.model.Job;

import java.util.List;

public class FavListViewModel extends AndroidViewModel {

    private final LiveData<List<Job>> favList;
    private FavoritesDB favoritesDB;

    public FavListViewModel(@NonNull Application application) {
        super(application);
        favoritesDB = FavoritesDB.getInstance(this.getApplication());
        favList = favoritesDB.getFavorites().getAllFavorites();
    }

    public LiveData<List<Job>> getFavList() {
        return favList;
    }

    public void deleteFav(Job job){
        new deleteJobAsyncTask(favoritesDB).execute(job);
    }

    public class deleteJobAsyncTask extends AsyncTask<Job, Void, Void>{
        private FavoritesDB favoritesDB;

        public deleteJobAsyncTask(FavoritesDB favoritesDB) {
            this.favoritesDB = favoritesDB;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            favoritesDB.getFavorites().deletefromFavorites(jobs[0]);
            return null;
        }
    }
}
