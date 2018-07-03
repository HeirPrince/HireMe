package com.nassaty.hireme.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.nassaty.hireme.room.db.JobDB;
import com.nassaty.hireme.model.Job;

import java.util.List;

public class JobListViewModel extends AndroidViewModel {

    private final LiveData<List<Job>> jobList;
    private JobDB jobDB;

    public JobListViewModel(@NonNull Application application) {
        super(application);
        jobDB = JobDB.getInstance(this.getApplication());
        jobList = jobDB.getDB().getAllNotes();
    }

    public LiveData<List<Job>> getJobList() {
        return jobList;
    }

    public void deleteJob(Job job){
        new deleteJobAsyncTask(jobDB).execute(job);
    }

    public class deleteJobAsyncTask extends AsyncTask<Job, Void, Void>{
        private JobDB jobDB;

        public deleteJobAsyncTask(JobDB jobDB) {
            this.jobDB = jobDB;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDB.getDB().deleteJob(jobs[0]);
            return null;
        }
    }
}
