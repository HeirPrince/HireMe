package com.nassaty.hireme.viewmodels;

import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.listeners.SingleJobListener;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.listeners.applicationRejectedListener;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class jobListViewModel extends AndroidViewModel {

    private String ref;
    private AuthUtils authUtils;
    applicationAddedListener listener;

    public jobListViewModel(@NonNull android.app.Application application) {
        super(application);
        authUtils = new AuthUtils(getApplication());
    }

    public void getJobList(final jobListListener listener){
        final List<Job> other_jobs = new ArrayList<>();
        final List<Job> my_jobs = new ArrayList<>();
        final List<Job> expensive_jobs = new ArrayList<>();


        FirebaseFirestore.getInstance()
                .collection(Constants.jobRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc != null) {
                                Job job = doc.toObject(Job.class);
                                my_jobs.add(job);
                                listener.jobList(my_jobs);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public interface fetchListener {
        void gottenList(List<Application> applications);
    }

    public void fetchApplications(String ref_id, final fetchListener fetchListener) {
        this.ref = ref_id;
        final List<Application> applications = new ArrayList<>();

        CollectionReference appRef = FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .document(ref_id)
                .collection(Constants.REQUESTED);

        appRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc != null) {
                                Application application = doc.toObject(Application.class);
                                applications.add(application);
                                fetchListener.gottenList(applications);
                            }
                        }
                    }
                });
    }

    public void getJObsByUID(final String phone, final jobListListener listener) {
        final List<Job> jobs = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection(Constants.jobRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc != null) {
                                Job job = doc.toObject(Job.class);
                                if (job.getOwner().equals(phone)) {
                                    jobs.add(job);
                                    listener.jobList(jobs);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public String getRef() {
        return ref;
    }

    public void sendApplication(final Application application, final applicationAddedListener addedListener) {
        changeAppState(application, true, false, false);
        FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .document(getRef())
                .collection(Constants.REQUESTED)
                .document(application.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        shiftToAccepted(application, new applicationAddedListener() {
                            @Override
                            public void applicationAdded(Boolean state) {
                                if (state) {
                                    addedListener.applicationAdded(true);
                                } else {
                                    addedListener.applicationAdded(false);
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addedListener.applicationAdded(false);
                    }
                });
    }

    public void shiftToAccepted(Application application, final applicationAddedListener listener) {
        changeAppState(application, false, true, false);
        FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .document(application.getJob_id())
                .collection(Constants.ACCEPTED)
                .document(application.getId())
                .set(application)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            listener.applicationAdded(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.applicationAdded(false);
                    }
                });
    }

    public void rejectApplication(final Application application, final applicationRejectedListener listener) {
        changeAppState(application, false, false, true);
        FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .document(getRef())
                .collection(Constants.REJECTED)
                .document(application.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        shiftToAccepted(application, new applicationAddedListener() {
                            @Override
                            public void applicationAdded(Boolean state) {
                                if (state) {
                                    listener.isRejected(true);
                                } else {
                                    listener.isRejected(false);
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.isRejected(false);
                    }
                });
    }

    public void changeAppState(Application application, boolean sent, boolean accepted, boolean rejected) {
        application.setSent(sent);
        application.setAccepted(accepted);
        application.setRejected(rejected);
    }

    public void deleteJob(String ref) {
        FirebaseFirestore.getInstance()
                .collection(Constants.jobRef)
                .document(ref)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication(), "job deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplication(), "application not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getJobByRef(String ref, final SingleJobListener callback){
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


}
