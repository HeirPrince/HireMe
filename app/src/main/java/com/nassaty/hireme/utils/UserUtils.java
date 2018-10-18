package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

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

    public void rateApplicant(String applicant_id, String rating_uid, float val, isRated isRated){
	    Map<String, Float> rate = new HashMap<>();
	    rate.put(rating_uid, val);

        firebaseFirestore.collection("Rating").document(applicant_id).collection("Clients")
                .add(rate)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            isRated.rated(true);
                        }else {
                            isRated.rated(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isRated.rated(false);
            }
        });
    }

    public void getUserRating(String uid, userStatsListener ratingListener){
        firebaseFirestore.collection("Rating").document(uid).collection("Clients")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e == null){
                            Toast.makeText(context, "Couldn't rate user try again", Toast.LENGTH_SHORT).show();
                        }

                        float total = 0;
                        float count = 0;
                        float average= 0;

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (documentSnapshot.exists()){
                                float rating = documentSnapshot.toObject(float.class);
                                total = total + rating;
                                count = count + 1;
                                average = total / count;

                                Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();

                                setAverageRating(uid, average);
                            }
                        }

                    }
                });
    }

    private void setAverageRating(String uid, float average) {
        Map<String, Float> rate = new HashMap<>();
        rate.put("rating", average);

        firebaseFirestore.collection("Rating").document(uid).collection("Average Rating")
                .add(rate)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Average rating set successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Average rating not set", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProfileData(String uid, final userByUid userByUid){
        firebaseFirestore.collection(Constants.userRef)
                .whereEqualTo("uid", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e == null){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            User user = doc.toObject(User.class);
                            if (user != null){
                                userByUid.user(user);
                            }else {
                                userByUid.user(null);
                            }
                        }
                    }
                });
    }

    public interface userByUid{
        void user(User user);
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
