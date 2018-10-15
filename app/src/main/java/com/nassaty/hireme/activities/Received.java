package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.ApplicationAdapter;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

public class Received extends AppCompatActivity {

	Toolbar toolbar;
	RecyclerView received_list;
	ApplicationUtils applicationUtils;
	ApplicationAdapter adapter;
	List<Application> apps;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_received);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Applicants");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		applicationUtils = new ApplicationUtils(this);
		apps = new ArrayList<>();

		progressBar = findViewById(R.id.progress);
		received_list = findViewById(R.id.received_list);
		received_list.setLayoutManager(new LinearLayoutManager(this));
		received_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

		adapter = new ApplicationAdapter(this, apps);
		received_list.setAdapter(adapter);

		getApplicants(getIntent().getStringExtra("job_ref"));
	}

	public void getApplicants(String ref){
		toggleProgress(true);
		applicationUtils.getAppsByJobId(ref, applications -> {
			if (applications != null){
				toggleProgress(false);
				apps.addAll(applications);
				adapter.notifyDataSetChanged();
			}else {
				toggleProgress(false);
				Toast.makeText(Received.this, "no applications found for this job", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void toggleProgress(boolean b) {
		if (b)
			progressBar.setVisibility(View.VISIBLE);
		else
			progressBar.setVisibility(View.GONE);
	}

}
