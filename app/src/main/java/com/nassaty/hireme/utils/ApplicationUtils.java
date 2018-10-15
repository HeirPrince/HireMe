package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUtils {

	Context context;
	FirebaseFirestore firebaseFirestore;
	NotificationUtils notificationUtils;
	onAppFoundListener onAppFoundListener;

	public ApplicationUtils(Context context) {
		this.context = context;
		this.firebaseFirestore = FirebaseFirestore.getInstance();
		this.notificationUtils = new NotificationUtils(context);
	}

	public interface onAppFoundListener{
		void foundApp(List<Application> applications);
	}

	public void getAppsByJobId(String id, final onAppFoundListener onAppFoundListener){

		final List<Application> applications = new ArrayList<>();

		firebaseFirestore.collection(Constants.applicationRef)
				.whereEqualTo("job_id", id)
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

	public interface jobDone{
		void onJobDone(Boolean done);
	}

	public void deleteApplication(final String notif_id, final jobDone jobDone){
		if (!notif_id.equals("")){
			firebaseFirestore.collection(Constants.applicationRef).document(notif_id)
					.delete()
					.addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							jobDone.onJobDone(true);
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							jobDone.onJobDone(false);
						}
					});
		}else {
			Toast.makeText(context, "reference returns null", Toast.LENGTH_SHORT).show();
		}
	}

	public interface onTasksFound{
		void task(List<com.nassaty.hireme.model.Task> tasks, int count);
	}

	public void getTasks(String ref, final onTasksFound onTasksFound){
		firebaseFirestore.collection(Constants.applicationRef).document(ref).collection("requested")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						for (QueryDocumentSnapshot snapshot : task.getResult()){
							if (snapshot.exists()){
								Application application = snapshot.toObject(Application.class);
								onTasksFound.task(application.getTasks(), application.getTasks().size());
							}
							else {
								Toast.makeText(context, "tasks not found", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}




}
