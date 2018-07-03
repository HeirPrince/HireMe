package com.nassaty.hireme.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.nassaty.hireme.room.db.JobDB;
import com.nassaty.hireme.model.Job;

public class NewJobViewModel extends AndroidViewModel {

    private JobDB jobDB;

    public NewJobViewModel(@NonNull Application application) {
        super(application);

        jobDB = JobDB.getInstance(this.getApplication());
    }

    public void addJob(final Job job){
        new addAsyncTask(jobDB).execute(job);
    }

    public class addAsyncTask extends AsyncTask<Job, Void, Void>{

        private JobDB jobDB;

        public addAsyncTask(JobDB jobDB) {
            this.jobDB = jobDB;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDB.getDB().insertJob(jobs[0]);
            return null;
        }
    }

}
