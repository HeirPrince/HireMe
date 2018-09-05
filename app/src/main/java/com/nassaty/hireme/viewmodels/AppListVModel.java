package com.nassaty.hireme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.listeners.SingleAppListener;
import com.nassaty.hireme.listeners.appListListener;
import com.nassaty.hireme.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AppListVModel extends AndroidViewModel {

    public AppListVModel(@NonNull Application application) {
        super(application);
    }

    public void getAppByUID(String id, String uid, final SingleAppListener appListener){

        FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .document(id)
                .collection(Constants.REQUESTED)
                .whereEqualTo("sender", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot doc = task.getResult();
                            for (DocumentSnapshot snapshot : doc.getDocuments()){
                                if (snapshot.exists()){
                                    com.nassaty.hireme.model.Application application = snapshot.toObject(com.nassaty.hireme.model.Application.class);
                                    appListener.isFound(true, application);
                                }else {
                                    appListener.isFound(false, null);
                                }
                            }
                        }
                    }
                });
    }

    public void getAppsByJobID(String id, final appListListener callback){
        final List<com.nassaty.hireme.model.Application> applications = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection(Constants.applicationRef)
                .document(id)
                .collection(Constants.REQUESTED)
                .whereEqualTo("job_id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            if (snapshot.exists()){
                                com.nassaty.hireme.model.Application application = snapshot.toObject(com.nassaty.hireme.model.Application.class);
                                applications.add(application);
                                callback.appList(applications);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.appList(null);
                    }
                });
    }
}
