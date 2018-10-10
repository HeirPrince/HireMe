package com.nassaty.hireme.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.JobAdapter;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.viewmodels.SharedViewModel;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

	RelativeLayout plc_holder;
	Toolbar toolbar;
	ProgressBar progressBar;
	RecyclerView results;
	JobAdapter jobAdapter;
	EditText searchField;
	com.nassaty.hireme.viewmodels.jobListViewModel jobListViewModel;
	SharedViewModel sharedViewModel;
	List<Job> jobList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Search");

		jobList = new ArrayList<>();
		jobAdapter = new JobAdapter(this, jobList);


		searchField = findViewById(R.id.editText);
		plc_holder = findViewById(R.id.plc_search);
		progressBar = findViewById(R.id.progress);
		progressBar.setVisibility(View.VISIBLE);
		
		results = findViewById(R.id.result_list);
		results.setLayoutManager(new LinearLayoutManager(this));
		results.setItemAnimator(new DefaultItemAnimator());
		results.setHasFixedSize(true);
		results.setAdapter(jobAdapter);
		results.setVisibility(View.GONE);

		sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
		jobListViewModel = ViewModelProviders.of(this).get(jobListViewModel.class);

		searchField.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.equals("")){
					plc_holder.setVisibility(View.VISIBLE);
					results.setVisibility(View.GONE);
				}else {
					FilterJob(s.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});

//		getData();
	}

	public void FilterJob(String s){
		if (s.equals("")){
			progressBar.setVisibility(View.GONE);
			results.setVisibility(View.GONE);
			plc_holder.setVisibility(View.VISIBLE);
		}else {
			jobListViewModel.filter(s, new jobListListener() {
				@Override
				public void jobList(List<Job> jobs) {
					if (jobs != null){
						progressBar.setVisibility(View.GONE);
						jobList.clear();
						jobList.addAll(jobs);
						jobAdapter.notifyDataSetChanged();
						results.setVisibility(View.VISIBLE);
						plc_holder.setVisibility(View.GONE);
						results.setAdapter(jobAdapter);
					}else {
						progressBar.setVisibility(View.GONE);
						results.setVisibility(View.GONE);
						plc_holder.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}

	private void getData() {
		progressBar.setVisibility(View.VISIBLE);
		jobListViewModel.getJobList(new jobListListener() {
			@Override
			public void jobList(List<Job> jobs) {
				if (jobs == null){
					progressBar.setVisibility(View.GONE);
					Toast.makeText(SearchResultsActivity.this, "no jobList found", Toast.LENGTH_SHORT).show();
				}else {
					progressBar.setVisibility(View.GONE);
					jobList.clear();
					jobList.addAll(jobs);
					jobAdapter.notifyDataSetChanged();
				}
			}
		});
	}


	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {

//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//			String query = intent.getStringExtra(SearchManager.QUERY);
//			//use the query to search your data somehow
//			qry.setText(query);
//		}
	}
}
