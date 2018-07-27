package com.nassaty.hireme.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nassaty.hireme.listeners.updateListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.Constants;

import java.util.Map;

public class NewJobModel{

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public NewJobModel(Context context) {
        this.context = context;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void insertJob(Job job){
        DocumentReference documentReference = firebaseFirestore.collection(Constants.jobRef).document();
        String id = documentReference.getId();
        DocumentReference reference = firebaseFirestore.collection(Constants.jobRef).document(id);
        job.setId(id);
        reference.set(job)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteJob(String id){
        //delete a job in my jobs activity
    }

    public void editJob(String ref, Map<String, Object> job, final updateListener listener){
        //updates job's data
        DocumentReference jobRef = firebaseFirestore.collection(Constants.jobRef).document(ref);
        listener.isUpdating(true);
        jobRef
                .update(job)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            listener.isDone(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        listener.isFailed(true);
                    }
                });

    }

    public void editSalary(String ref, String field, Integer salary, final updateListener listener){
        //updates job's salary
        DocumentReference jobRef = firebaseFirestore.collection(Constants.jobRef).document(ref);
        listener.isUpdating(true);
        jobRef
                .update(field, salary)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            listener.isDone(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.isFailed(true);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }



}
