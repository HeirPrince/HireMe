package com.nassaty.hireme.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class JobUtils {

    FirebaseFirestore firebaseFirestore;
    onJobFoundListener onJobFoundListener;
    jobListCallback jobListCallback;

    public JobUtils() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public interface onJobFoundListener{
        void foundJob(Job job);
    }

    public interface jobListCallback{
        void jobs(List<Job> jobs);
    }

    public void getJobById(String ref, final onJobFoundListener callback){
        FirebaseFirestore.getInstance()
                .collection(Constants.jobRef)
                .document(ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){
                                Job job = snapshot.toObject(Job.class);
                                callback.foundJob(job);
                            }else {
                                callback.foundJob(null);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.foundJob(null);
                    }
                });
    }

    public void getJobsByUID(String uid, final jobListCallback callback){
        final List<Job> jobs = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection(Constants.jobRef)
                .whereEqualTo("job_id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc != null) {
                                    Job job = doc.toObject(Job.class);
                                    jobs.add(job);

                                    callback.jobs(jobs);
                                }else
                                    callback.jobs(null);
                            }
                        }
                    }
                });
    }

    public void getJobCount(String uid, statsListener listener){
        firebaseFirestore.collection(Constants.jobRef)
                .whereEqualTo("owner", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.d("TAG", e.getMessage());
                        }

                        int total = 0;
                        int count = 0;

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            if (snapshot.exists()){
                                count = count+1;
                                listener.jobs(count);
                            }
                        }
                    }
                });
    }

    public interface statsListener{
        void jobs(int val);
    }


}
