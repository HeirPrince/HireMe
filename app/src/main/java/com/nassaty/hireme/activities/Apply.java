package com.nassaty.hireme.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.TaskAdapter;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.model.Task;
import com.nassaty.hireme.viewmodels.NewApplicationVModel;

import java.util.ArrayList;
import java.util.List;

public class Apply extends AppCompatActivity {

	RecyclerView task_list;
	TaskAdapter adapter;
	List<Task> tasks;
	NewApplicationVModel applicationVModel;
	EditText title, salary;
	TextView total;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply);
		tasks = new ArrayList<>();

		task_list = findViewById(R.id.task_list);
		title = findViewById(R.id.title);
		salary = findViewById(R.id.salary);
		total = findViewById(R.id.total);
		applicationVModel = ViewModelProviders.of(this).get(NewApplicationVModel.class);

		updateUI();
	}

	public void updateUI(){

		task_list.setLayoutManager(new LinearLayoutManager(this));
		task_list.setHasFixedSize(true);
	}

	public void addTask(View view) {
		Task task = new Task();
		task.setTitle(title.getText().toString());
		task.setSalary(Integer.valueOf(salary.getText().toString()));

		tasks.add(task);
		adapter = new TaskAdapter(this, tasks);
		task_list.setAdapter(adapter);
	}

	public void hireApplicant(View view) {
		String ref_id = getIntent().getStringExtra("ref_id");

		applicationVModel.sendApplication(ref_id, tasks, new applicationAddedListener() {
			@Override
			public void applicationAdded(Boolean state) {
				if (state) {
					Toast.makeText(Apply.this, "application added successfully", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(Apply.this, "application not added or may have been cancelled", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
