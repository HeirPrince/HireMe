package com.nassaty.hireme.viewmodels;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.Constants;


public class UserVModel {

    public static FirebaseFirestore firebaseFirestore;
    public FirebaseStorage storage;
    static CollectionReference userRef;

    public UserVModel() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public User getUserByUid(String uid, final userByUid userByUid){
        firebaseFirestore.collection(Constants.userRef)
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                userByUid.user(user);
                            }
                        } else {
                            userByUid.user(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userByUid.user(null);
                    }
                });
        return null;
    }

    public interface userByUid{
        void user(User user);
    }
}
