package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.TaskAdapter;
import com.nassaty.hireme.model.Task;
import com.nassaty.hireme.utils.ApplicationUtils;

import java.util.List;

public class AppDetails extends AppCompatActivity {

	RecyclerView task_list;
	TaskAdapter adapter;
	ApplicationUtils applicationUtils;
	TextView total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_details);

		task_list = findViewById(R.id.task_list);
		total = findViewById(R.id.total);
		applicationUtils = new ApplicationUtils(this);

		updateUI();


	}

	public void updateUI(){

		final String ref =  getIntent().getStringExtra("ref");

		task_list.setLayoutManager(new LinearLayoutManager(this));
		task_list.setHasFixedSize(true);

		applicationUtils.getTasks(ref, new ApplicationUtils.onTasksFound() {
			@Override
			public void task(List<Task> tasks, int count) {
				if (tasks != null){
					adapter = new TaskAdapter(AppDetails.this, tasks);
					task_list.setAdapter(adapter);
				}else {
					Toast.makeText(AppDetails.this, "no tasks for "+ref, Toast.LENGTH_SHORT).show();
				}
			}
		});

	}



	public void hireApplicant(View view) {
		Toast.makeText(this, "applicant hired, remember to rate him at the end of the job", Toast.LENGTH_SHORT).show();
	}
}
