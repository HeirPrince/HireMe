package com.nassaty.hireme.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.Worker_list;
import com.nassaty.hireme.model.Employee;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class EmpUtils {

	Context context;
	public List<String> employees;
	FirebaseFirestore firebaseFirestore;
	UserUtils userUtils;

	public EmpUtils(Context context) {
		this.context = context;
		this.firebaseFirestore = FirebaseFirestore.getInstance();
		this.userUtils = new UserUtils(context);
	}

	public void getEmployees(String uid, final getEmpList list) {

		final List<Employee> emps = new ArrayList<>();

		firebaseFirestore.collection("employees")
				.whereEqualTo("employer_uid", uid)
				.addSnapshotListener(new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
						if (e != null) {
							Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
							return;
						}

						for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
							Employee employee = doc.toObject(Employee.class);
							emps.add(employee);
							list.list(emps);
						}
					}
				});

	}

	public void addEmployee(String employer_uid, String employee_uid){

		Employee employee = new Employee();

		employee.setEmployee_uid(employee_uid);
		employee.setEmployer_uid(employer_uid);

		firebaseFirestore.collection("employees")
				.add(employee)
				.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
					@Override
					public void onComplete(@NonNull Task<DocumentReference> task) {
						if (task.isSuccessful()){
							final Dialog confirmDialog = new Dialog(context);
							confirmDialog.setContentView(R.layout.dialog_applicant_confirm);

							Button dialog_cancel = confirmDialog.findViewById(R.id.cancel);
							Button dialog_view_list = confirmDialog.findViewById(R.id.view_list);

							dialog_cancel.setOnClickListener(v1 -> confirmDialog.dismiss());

							dialog_view_list.setOnClickListener(v12 -> {
								confirmDialog.dismiss();
								// rate applicant
								context.startActivity(new Intent(context, Worker_list.class));

							});

							confirmDialog.show();
						}else {
							Toast.makeText(context, "employee not added", Toast.LENGTH_SHORT).show();
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public interface getEmpList{
		void list(List<Employee> employees);
	}
}
