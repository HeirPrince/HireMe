package com.nassaty.hireme.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.listeners.SingleJobListener;
import com.nassaty.hireme.listeners.updateListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.viewmodels.NewJobModel;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.HashMap;
import java.util.Map;

public class EditJob extends AppCompatActivity {

    Toolbar toolbar;
    NewJobModel newJobModel;
    EditText job_title, job_desc, job_salary;
    View edit_title, edit_desc, edit_salary, done_title, done_salary, done_desc;
    ProgressBar loading_title, loading_desc, loading_salary;
    String ref, title;
    jobListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(jobListViewModel.class);

        ref = getIntent().getStringExtra("ref");
        title = getIntent().getStringExtra("job_title");

        getSupportActionBar().setTitle("Edit " + getT());

        newJobModel = new NewJobModel(this);

        //initiate views
        job_title = findViewById(R.id.jobTitle);
        job_desc = findViewById(R.id.jobDescription);
        edit_title = findViewById(R.id.edit_title);
        edit_desc = findViewById(R.id.edit_desc);
        edit_salary = findViewById(R.id.edit_salary);

        setViews();
    }

    public void setViews() {
        String ref = getIntent().getStringExtra("ref");
        Toast.makeText(this, ref, Toast.LENGTH_SHORT).show();
        viewModel.getJobByRef(ref, new SingleJobListener() {
            @Override
            public void foundJob(Job job) {
                if (job != null) {
                    job_title.setText(job.getTitle());
                    job_desc.setText(job.getDescription());
                } else {
                    Toast.makeText(EditJob.this, "job not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String getRef() {
        return ref;
    }


    public String getT() {
        return title;
    }

    public void update(View view) {

        String title = job_title.getText().toString();
        String desc = job_desc.getText().toString();
        int salary = Integer.valueOf(job_salary.getText().toString());

        Map<String, Object> updatedJob = new HashMap<>();
        updatedJob.put("title", title);
        updatedJob.put("description", desc);
        updatedJob.put("salary", salary);

        newJobModel.editJob(getRef(), updatedJob, new updateListener() {
            @Override
            public void isUpdating(Boolean updating) {

            }

            @Override
            public void isDone(Boolean done) {


            }

            @Override
            public void isFailed(Boolean failed) {

            }
        });
    }

    public void updateTitle(View view) {
        if (job_title.isEnabled())
            job_title.setEnabled(false);
        else
            edit_title.setVisibility(View.GONE);
            job_title.setEnabled(true);
    }

    public void updateDescription(View view) {
        if (job_desc.isEnabled())
            job_desc.setEnabled(false);
        else
            edit_desc.setVisibility(View.GONE);
            job_desc.setEnabled(true);
    }

    public void updateSalary(View view) {
        if (job_salary.isEnabled())
            job_salary.setEnabled(false);
        else
            edit_salary.setVisibility(View.GONE);
            job_salary.setEnabled(true);
    }
}
