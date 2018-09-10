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
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.utils.NotificationUtils;
import com.nassaty.hireme.utils.TimeUtils;
import com.nassaty.hireme.utils.UserUtils;

import java.util.Map;

public class NewJobModel{

    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private NotificationUtils notificationUtils;
    private AuthUtils authUtils;
    private UserUtils userUtils;
    private TimeUtils timeUtils;

    public NewJobModel(Context context) {
        this.context = context;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.notificationUtils = new NotificationUtils(context);
        this.authUtils = new AuthUtils(context);
        this.userUtils = new UserUtils();
        this.timeUtils = new TimeUtils();
    }

    public void insertJob(final Job job){
        DocumentReference documentReference = firebaseFirestore.collection(Constants.jobRef).document();
        String id = documentReference.getId();
        DocumentReference reference = firebaseFirestore.collection(Constants.jobRef).document(id);
        job.setId(id);
        reference.set(job)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                        sendNotification(job.getTitle());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sendNotification(final String job_title){

        userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
            @Override
            public void user(User user) {
                if (user != null){
                    Notif notif = new Notif();
                    notif.setType(1);
                    notif.setSender_uid(user.getUID());
                    notif.setReceiver_uid("");
                    notif.setText(notificationUtils.jobNotification(job_title, user.getUser_name()));
                    notif.setTime(timeUtils.getCurrentTimeStamp());

                    notificationUtils.sendNotification(notif);
                }
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
