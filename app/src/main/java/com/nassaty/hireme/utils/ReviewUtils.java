package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.model.Review;
import com.nassaty.hireme.model.User;

import java.util.ArrayList;
import java.util.List;

public class ReviewUtils {

    FirebaseFirestore firebaseFirestore;
    CollectionReference rev;
    private AuthUtils authUtils;
    private NotificationUtils notificationUtils;
    private TimeUtils timeUtils;
    private UserUtils userUtils;
    private JobUtils jobUtils;
    Context context;

    public ReviewUtils(Context context) {
        this.context = context;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.rev = firebaseFirestore.collection("reviews");
        this.authUtils = new AuthUtils(context);
        this.userUtils = new UserUtils();
        this.notificationUtils = new NotificationUtils(context);
        this.timeUtils = new TimeUtils();
        this.jobUtils = new JobUtils();
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

    public interface reviewReplied{
        void isReplied(Boolean reply);
    }

    /*  methods
    * ==================================
    * */

    public void addReview(final Review review, final reviewAddedListener listener){
        rev.add(review)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {
                        if (documentReference != null) {
                            listener.isAdded(true, "Review added");
                            jobUtils.getJobById(review.getJob_id(), new JobUtils.onJobFoundListener() {
                                @Override
                                public void foundJob(Job job) {
                                    if (job != null){
                                        sendNotification(review.getJob_id(), documentReference.getId());
                                    }
                                }
                            });

                        }
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

    public void sendNotification(final String job_id, final String rev_id){
        userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
            @Override
            public void user(final User user) {
                if (user != null) {
                    final Notif notif = new Notif();
                    notif.setType(0);
                    notif.setTime(timeUtils.getCurrentTimeStamp());
                    notif.setSender_uid(user.getUID());
                    notif.setContent_id(rev_id);
                    notif.setRead(false);

                    jobUtils.getJobById(job_id, new JobUtils.onJobFoundListener() {
                        @Override
                        public void foundJob(Job job) {
                            if (job != null) {
                                notif.setReceiver_uid(job.getOwner());
                                notif.setText(notificationUtils.revNotification(job.getTitle(), user.getUser_name()));

                                notificationUtils.sendNotification(notif);
                            }
                        }
                    });

                }
            }
        });
    }

    // FIXME: 9/13/2018 search for update codes
    public void ReplyReview(String ref, String text, final reviewReplied replied){
//        Map<String, String> reply = new HashMap<>();
//        reply.put("reply", text);
//
//        rev.document(ref)
//                .update(reply)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        replied.isReplied(true);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        replied.isReplied(false);
//                    }
//                })
    }
}
