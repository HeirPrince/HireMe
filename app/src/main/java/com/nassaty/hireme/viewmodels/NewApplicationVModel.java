package com.nassaty.hireme.viewmodels;

import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;

import java.util.UUID;

public class NewApplicationVModel extends AndroidViewModel {

    CollectionReference appRef;
    applicationAddedListener addedListener;
    private AuthUtils authUtils;
    Constants constants;

    public NewApplicationVModel(@NonNull android.app.Application application) {
        super(application);
        authUtils = new AuthUtils(getApplication());
        constants = new Constants();
    }

    public void sendApplication(String ref_id, final applicationAddedListener addedListener1) {
        Application application = new Application();
        application.setJob_id(ref_id);
        application.setId(UUID.randomUUID().toString());
        application.setStatusCode(1);
        application.setSender(authUtils.getCurrentUser().getUid());

        FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .add(application)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                            addedListener1.applicationAdded(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addedListener1.applicationAdded(false);
                    }
                });
    }
}
