package com.nassaty.hireme.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class Constants {

    public static final String REQUESTED = "requested";
    public static final String ACCEPTED = "accepted";
    public static final String REJECTED = "rejected";
    CollectionReference requestedApps;
    CollectionReference acceptedApps;
    CollectionReference rejectedApps;

    public static final String applicationRef = "applications";
    public static final String jobRef = "jobs";
    public static final String my_jobsRef = "my_jobs";
    public static final String accepted = "accepted_applications";
    public static final String denied = "denied_applications";
    public static String randomID = UUID.randomUUID().toString();

    public CollectionReference getRequestedApps(String job_id) {
        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection(applicationRef)
                .document(job_id)
                .collection(REQUESTED);
        return ref;
    }

    public CollectionReference getAcceptedApps(String job_id) {
        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection(applicationRef)
                .document(job_id)
                .collection(ACCEPTED);
        return ref;
    }

    public CollectionReference getRejectedApps(String job_id) {
        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection(applicationRef)
                .document(job_id)
                .collection(REJECTED);
        return ref;
    }

    public String getRandomID() {
        return randomID;
    }
}
