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

    /*Pop menu with icons*
    PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.filter_popup_menu, popupMenu.getMenu());

                try {
                    Field[] fields = popupMenu.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popupMenu);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.show();/
     */
}
