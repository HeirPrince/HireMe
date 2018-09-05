package com.nassaty.hireme.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewUtils {

    FirebaseFirestore firebaseFirestore;
    CollectionReference rev;

    public ReviewUtils() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.rev = firebaseFirestore.collection("reviews");
    }

    /* interfaces
    * ==================================
    * */

    public interface getReviewList{
        void reviews(List<Review> reviews);
    }

    public interface reviewAddedListener{
        void isAdded(Boolean isAdded, String message);
    }

    /*  methods
    * ==================================
    * */

    public void addReview(Review review, final reviewAddedListener listener){
        rev.add(review)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                            listener.isAdded(true, "Review added");
                        else
                            listener.isAdded(false, "Your review couldn't be added try again");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.isAdded(false, "Your review couldn't be added try again");
                    }
                });
    }

    public void loadReviews(String job_id, final getReviewList setReviews){
        final List<Review> reviews = new ArrayList<>();

        rev.whereEqualTo("job_id", job_id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot documentSnapshot : snapshot.getDocuments()){
                    if (documentSnapshot.exists()){
                        Review review = documentSnapshot.toObject(Review.class);
                        reviews.add(review);
                        setReviews.reviews(reviews);
                    }
                    setReviews.reviews(null);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 setReviews.reviews(null);
             }
        });
    }

    public void ReplyReview(String ref){
        //employee replies a review
    }
}
