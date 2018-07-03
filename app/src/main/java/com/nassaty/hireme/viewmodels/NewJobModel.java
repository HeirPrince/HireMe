package com.nassaty.hireme.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class NewJobModel{

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public NewJobModel(Context context) {
        this.context = context;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void insertJob(Job job){
        CollectionReference reference = firebaseFirestore.collection(Constants.jobRef);
        reference.add(job)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
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
        //delete a job when in my jobs activity
    }

    public void editJob(){

    }

    public void getJobList(String phone, final jobListListener listListener){
        final List<Job> jobs = new ArrayList<>();

        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection(Constants.jobRef)
                .document(phone)
                .collection(Constants.my_jobsRef);

        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e == null)
                    return;

                for (DocumentSnapshot doc : Objects.requireNonNull(queryDocumentSnapshots)){
                    if (doc.exists()){
                        Toast.makeText(context, "im there", Toast.LENGTH_SHORT).show();
                        Job job = doc.toObject(Job.class);
                        jobs.add(job);
                        listListener.jobList(jobs);
                    }else {
                        Toast.makeText(context, "im t", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


}
