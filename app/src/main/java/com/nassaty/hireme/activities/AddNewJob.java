package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.NewJobModel;
import com.shalan.mohamed.itemcounterview.IncDecView;

import java.util.ArrayList;
import java.util.List;

public class AddNewJob extends AppCompatActivity {

    private EditText jobTitle, jobDesc, jobSalary;
    IncDecView itemCounter;
    private NewJobModel newJobModel;
    private AuthUtils authUtils;
    Switch more_times;
    Spinner cats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job);

        jobTitle = findViewById(R.id.jobTitle);
        jobDesc = findViewById(R.id.jobDescription);
        jobSalary = findViewById(R.id.jobSalary);
        more_times = findViewById(R.id.more_times);
        cats = findViewById(R.id.spinner_cat);
        itemCounter = findViewById(R.id.itemCounter);

        setSpinnerCats();
        triggerTimes();




        authUtils = new AuthUtils(this);
        newJobModel = new NewJobModel(this);//TODO create its own viewmodel

    }

    private void setSpinnerCats() {
        List<String> categories = new ArrayList<>();
        categories.add("House Cleaning");
        categories.add("Driving");
        categories.add("Electricity Repair");
        categories.add("House hold");
        categories.add("Fashion designer");
        categories.add("Stock Manager");
        categories.add("Sales Manager");

        ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        cat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cats.setAdapter(cat_adapter);
    }

    private void triggerTimes() {

        more_times.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    itemCounter.setVisibility(View.VISIBLE);
                else
                    itemCounter.setVisibility(View.GONE);
            }
        });

    }

    public void addJob(View view) {
        if (jobDesc.getText() == null && jobTitle.getText() == null){
            Toast.makeText(this, "empty fields", Toast.LENGTH_SHORT).show();
        }else {
            Job job = new Job();
            job.setDescription(jobDesc.getText().toString());
            job.setSalary(Integer.valueOf(jobSalary.getText().toString()));
            job.setTitle(jobTitle.getText().toString());
            job.setId("kk");
            job.setOwner(authUtils.getCurrentUser().getUid());


//            newJobViewModel.addJob(new Job(jobTitle.getText().toString(), jobDesc.getText().toString(), Integer.valueOf(jobSalary.getText().toString())));
            newJobModel.insertJob(job);
            finish();
            startActivity(new Intent(AddNewJob.this, MainActivity.class));
        }
    }
}
