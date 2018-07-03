package com.nassaty.hireme.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.ApplicationAdapter;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.List;

public class AppList extends AppCompatActivity {

    RecyclerView recyclerView;
    ApplicationAdapter applicationAdapter;
    jobListViewModel applistVModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        recyclerView = findViewById(R.id.app_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        applistVModel = ViewModelProviders.of(this).get(jobListViewModel.class);
        applistVModel.fetchApplications(getIntent().getStringExtra("ref_id"), new jobListViewModel.fetchListener() {
            @Override
            public void gottenList(List<Application> applications) {
                applicationAdapter = new ApplicationAdapter(AppList.this, applications);
                recyclerView.setAdapter(applicationAdapter);
            }
        });





    }
}
