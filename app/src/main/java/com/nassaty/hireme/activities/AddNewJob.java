package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.NewJobModel;

public class AddNewJob extends AppCompatActivity {

    private EditText jobTitle, jobDesc, jobSalary;
    private NewJobModel newJobModel;
    private AuthUtils authUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job);

        jobTitle = findViewById(R.id.jobTitle);
        jobDesc = findViewById(R.id.jobDescription);
        jobSalary = findViewById(R.id.jobSalary);

        authUtils = new AuthUtils(this);
        newJobModel = new NewJobModel(this);//TODO create its own viewmodel

    }

    public void addJob(View view) {
        if (jobDesc.getText() == null && jobTitle.getText() == null){
            Toast.makeText(this, "empty fields", Toast.LENGTH_SHORT).show();
        }else {
//            newJobViewModel.addJob(new Job(jobTitle.getText().toString(), jobDesc.getText().toString(), Integer.valueOf(jobSalary.getText().toString())));
            newJobModel.insertJob(new Job(jobTitle.getText().toString(), jobDesc.getText().toString(), Integer.valueOf(jobSalary.getText().toString()), authUtils.getCurrentUser().getUid()));
            finish();
            startActivity(new Intent(AddNewJob.this, MainActivity.class));
        }
    }
}
