package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.Notif;

import java.util.ArrayList;
import java.util.List;

public class NotificationUtils {

    Context context;
    FirebaseFirestore firebaseFirestore;
    AuthUtils authUtils;
    CollectionReference notifRef;

    public NotificationUtils(Context context) {
        this.context = context;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.authUtils = new AuthUtils(context);
        this.notifRef = firebaseFirestore.collection("notifications");
    }

    public void sendNotification(Notif notif) {

        notifRef.add(notif)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(context, "notification sent successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sendApplicationNotification(Notif notif) {
        notifRef.document("applications").collection("requested")
                .add(notif)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(context, "app notif sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String jobNotification(String job_title, String sender_username) {
        return sender_username + " Posted a job : " + job_title;
    }

    public String appNotification(String job_title, String sender_username) {
        return sender_username + " Applied for " + job_title;
    }

    public String revNotification(String job_title, String sender_username) {
        return sender_username + " Reviewed " + job_title;
    }


    public interface getNotificationList{
        void notifications(List<Notif> notifs);
    }
    public void loadNotifications(final getNotificationList list){

        final List<Notif> notifs = new ArrayList<>();

        notifRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        for (DocumentSnapshot item : snapshot.getDocuments()){
                            if (item.exists()){
                                Notif notif = item.toObject(Notif.class);
                                notifs.add(notif);
                                list.notifications(notifs);
                            }
                            else
                                list.notifications(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        list.notifications(null);
                    }
                });
    }

}
