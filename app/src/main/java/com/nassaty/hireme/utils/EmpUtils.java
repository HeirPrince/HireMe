package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class EmpUtils {

	Context context;
	public List<String> employees;
	FirebaseFirestore firebaseFirestore;

	public EmpUtils(Context context) {
		this.context = context;
		this.firebaseFirestore = FirebaseFirestore.getInstance();
	}

	public interface getEmpList{
		void list(List<String> employees);
	}

	public void getEmployees(String uid, final getEmpList list) {

		final List<String> emps = new ArrayList<>();

		firebaseFirestore.collection(Constants.userRef).whereEqualTo("uid", uid)
				.addSnapshotListener(new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
						if (e != null) {
							Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
							return;
						}


						for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
							User user = doc.toObject(User.class);
							emps.addAll(user.getEmployees());
							list.list(emps);
						}
					}
				});

	}

	public void setEmployees(String id, List<String> employees) {
		firebaseFirestore.collection(Constants.userRef).document(id)
				.update("employees", employees)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()){
							Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public void addEmployee(String id, String emp_id){
		firebaseFirestore.collection(Constants.userRef).document(id)
				.update("employees", emp_id)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()){
							Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}
