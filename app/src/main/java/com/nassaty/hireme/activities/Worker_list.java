package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.EmployeeAdapter;
import com.nassaty.hireme.model.Employee;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.EmpUtils;

import java.util.ArrayList;
import java.util.List;

public class Worker_list extends AppCompatActivity {

	Toolbar toolbar;
	RecyclerView employee_list;
	EmpUtils empUtils;
	AuthUtils authUtils;
	EmployeeAdapter employeeAdapter;
	List<Employee> emps;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_list);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Employees");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		empUtils = new EmpUtils(this);
		authUtils = new AuthUtils(this);

		emps = new ArrayList<>();
		employeeAdapter = new EmployeeAdapter(this, emps);

		progressBar = findViewById(R.id.progress);
		toggleProgress(true);
		employee_list = findViewById(R.id.worker_list);
		employee_list.setLayoutManager(new LinearLayoutManager(this));
		employee_list.setAdapter(employeeAdapter);

		loadData();

	}

	private void toggleProgress(boolean b) {
		if (b)
			progressBar.setVisibility(View.VISIBLE);
		else
			progressBar.setVisibility(View.GONE);
	}

	private void loadData() {
		empUtils.getEmployees(authUtils.getCurrentUser().getUid(), employees -> {
			if (employees != null){
				toggleProgress(false);
				emps.clear();
				emps.addAll(employees);
				employeeAdapter.notifyDataSetChanged();
			}else {
				toggleProgress(false);
				Toast.makeText(Worker_list.this, "no employees found for "+authUtils.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
				employeeAdapter.notifyDataSetChanged();
			}
		});
	}
}
