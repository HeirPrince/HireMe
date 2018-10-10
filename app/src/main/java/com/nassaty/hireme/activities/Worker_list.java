package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.nassaty.hireme.R;

public class Worker_list extends AppCompatActivity {

	Toolbar toolbar;
	RecyclerView employee_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_list);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Employees");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		employee_list = findViewById(R.id.worker_list);
		employee_list.setLayoutManager(new LinearLayoutManager(this));
		

	}
}
