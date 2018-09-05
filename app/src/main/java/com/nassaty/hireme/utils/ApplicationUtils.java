package com.nassaty.hireme.utils;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUtils {

	FirebaseFirestore firebaseFirestore;
	onAppFoundListener onAppFoundListener;

	public ApplicationUtils() {
		firebaseFirestore = FirebaseFirestore.getInstance();
	}

	public interface onAppFoundListener{
		void foundApp(List<Application> applications);
	}

	public void getAppsByJobId(String id, final onAppFoundListener onAppFoundListener){

		final List<Application> applications = new ArrayList<>();

		firebaseFirestore.collection(Constants.applicationRef).document(id).collection("requested")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						for (QueryDocumentSnapshot doc : task.getResult()) {
							if (doc != null) {
								Application application = doc.toObject(Application.class);

								applications.add(application);
								onAppFoundListener.foundApp(applications);
							}else
								onAppFoundListener.foundApp(null);
						}
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						onAppFoundListener.foundApp(null);
					}
				});
	}

	public void getAppsByUid(String uid, final onAppFoundListener onAppFoundListener){
		final List<Application> applications = new ArrayList<>();

		firebaseFirestore.collection(Constants.applicationRef)
				.whereEqualTo("sender", uid)
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						for (QueryDocumentSnapshot doc : task.getResult()) {
							if (doc != null) {
								Application application = doc.toObject(Application.class);

								applications.add(application);
								onAppFoundListener.foundApp(applications);
							}else
								onAppFoundListener.foundApp(null);
						}
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						onAppFoundListener.foundApp(null);
					}
				});
	}


}
