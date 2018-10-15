package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.User;

import java.util.List;

public class UserUtils {

    Context context;
    FirebaseFirestore firebaseFirestore;
    foundUser foundUser;
    JobUtils jobUtils;
    ApplicationUtils applicationUtils;
    FirebaseDatabase firebaseDatabase;

    public UserUtils(Context context) {
        this.context = context;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.jobUtils = new JobUtils();
        this.applicationUtils = new ApplicationUtils(context);
    }

    public interface foundUser{
        void user(User user);
    }

    public void getUserByUID(String uid, final foundUser foundUser){
        firebaseFirestore.collection(Constants.userRef)
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            if (snapshot.exists()){
                                User user = snapshot.toObject(User.class);
                                foundUser.user(user);
                            }else {
                                foundUser.user(null);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        foundUser.user(null);
                    }
                });
    }

    public void rateApplicant(String uid, int val, isRated isRated){
        firebaseFirestore.collection(Constants.userRef).document(uid)
                .update("stats", val)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            isRated.rated(true);
                        }else {
                            isRated.rated(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isRated.rated(false);
                    }
                });
    }

    public void getUserRating(String uid, userStatsListener ratingListener){
        firebaseFirestore.collection(Constants.userRef).document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);

                        if (user != null && user.getRating() >= 1){

                            jobUtils.getJobsByUID(uid, new JobUtils.jobListCallback() {
                                @Override
                                public void jobs(List<Job> jobs) {
                                    int count = jobs.size();
                                    applicationUtils.getAppsByUid(uid, new ApplicationUtils.onAppFoundListener() {
                                        @Override
                                        public void foundApp(List<Application> applications) {
                                            int app_count = applications.size();
                                            ratingListener.stats(user.getRating(), count, app_count);
                                        }
                                    });
                                }
                            });
                        }else {
                            ratingListener.stats(-1, -1, -1);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getUserByLocation(){
        // FIXME: 10/12/2018 Nmapactivity
    }

    public interface isRated{
        void rated(Boolean isUserRated);
    }

    public interface userStatsListener {
        void stats(int rating, int job_count, int app_count);
    }
}
