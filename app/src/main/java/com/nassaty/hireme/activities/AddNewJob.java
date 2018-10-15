package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.NewJobModel;

import java.util.ArrayList;
import java.util.List;

public class AddNewJob extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    private EditText jobTitle, jobDesc;
    private NewJobModel newJobModel;
    private AuthUtils authUtils;
    Spinner cats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post a Job");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jobTitle = findViewById(R.id.jobTitle);
        jobDesc = findViewById(R.id.jobDescription);
        cats = findViewById(R.id.spinner_cat);

        setSpinnerCats();

        authUtils = new AuthUtils(this);
        newJobModel = new NewJobModel(this);

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

    public void addJob(View view) {
        if (jobDesc.getText() == null && jobTitle.getText() == null && cats.getSelectedItem() == null){
            Toast.makeText(this, "empty fields", Toast.LENGTH_SHORT).show();
        }else {
            Job job = new Job();
            job.setDescription(jobDesc.getText().toString());
            job.setTitle(jobTitle.getText().toString());
            job.setId("kk");
            job.setCategory(cats.getSelectedItem().toString());
            job.setOwner(authUtils.getCurrentUser().getUid());

            newJobModel.insertJob(job);
            finish();
            startActivity(new Intent(AddNewJob.this, MainActivity.class));
        }
    }
}
