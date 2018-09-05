package com.nassaty.hireme.utils;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.model.User;

public class UserUtils {

    FirebaseFirestore firebaseFirestore;
    foundUser foundUser;

    public UserUtils() {
        firebaseFirestore = FirebaseFirestore.getInstance();
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
                            User user = snapshot.toObject(User.class);
                            foundUser.user(user);
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
}
