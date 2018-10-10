package com.nassaty.hireme.viewmodels;

import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.NotificationUtils;
import com.nassaty.hireme.utils.TimeUtils;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

public class NewApplicationVModel extends AndroidViewModel {

    CollectionReference appRef;
    applicationAddedListener addedListener;
    private AuthUtils authUtils;
    private NotificationUtils notificationUtils;
    private TimeUtils timeUtils;
    private UserUtils userUtils;
    private JobUtils jobUtils;
    Constants constants;

    public NewApplicationVModel(@NonNull android.app.Application application) {
        super(application);
        authUtils = new AuthUtils(getApplication());
        constants = new Constants();
        this.notificationUtils = new NotificationUtils(application);
        this.timeUtils = new TimeUtils();
        this.userUtils = new UserUtils();
        this.jobUtils = new JobUtils();
    }

    private int calcTotal(List<com.nassaty.hireme.model.Task> tasks) {
        int totalPrice = 0;

        for (int i = 0; i < tasks.size(); i++){
            totalPrice += tasks.get(i).getSalary();
        }

        return totalPrice;
    }

    public void sendApplication(String ref_id, List<com.nassaty.hireme.model.Task> tasks, final applicationAddedListener addedListener1) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final Application application = new Application();
        application.setJob_id(ref_id);
        application.setStatusCode(1);
        application.setTasks(tasks);
        application.setSalary(calcTotal(tasks));
        application.setSender(authUtils.getCurrentUser().getUid());

        DocumentReference idRef = firebaseFirestore.collection(Constants.applicationRef).document();

        String id = idRef.getId();
        DocumentReference appRef = firebaseFirestore.collection(Constants.applicationRef).document(id);
        application.setId(id);

        if (!application.getId().equals("")){
            appRef
                    .set(application)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addedListener1.applicationAdded(true);
                                Toast.makeText(getApplication(), application.getId(), Toast.LENGTH_SHORT).show();
                                sendNotification(application.getJob_id(), application.getId());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addedListener1.applicationAdded(false);
                }
            });
        }else {
            Toast.makeText(getApplication(), "Application id failure", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendNotification(final String jobId, final String appId){
        userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
            @Override
            public void user(final User user) {
                if (user != null) {
                    final Notif notif = new Notif();
                    notif.setType(2);
                    notif.setTime(timeUtils.getCurrentTimeStamp());
                    notif.setSender_uid(user.getUID());
                    notif.setContent_id(appId);
                    notif.setRead(false);
                    jobUtils.getJobById(jobId, new JobUtils.onJobFoundListener() {
                        @Override
                        public void foundJob(Job job) {
                            if (job != null) {
                                notif.setReceiver_uid(job.getOwner());
                                notif.setText(notificationUtils.appNotification(job.getTitle(), user.getUser_name()));

                                notificationUtils.sendNotification(notif);
                            }
                        }
                    });


                }
            }
        });
    }
}
